package gui.custom_elements;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import functionalities.sequence_editor.main_tools.sequence_conversion.IgnoreNumberingHtmlConverter;
import functionalities.sequence_editor.main_tools.sequence_conversion.SequenceToHtmlConverter;
import functionalities.sequence_editor.services.ChangeLineLengthService;
import functionalities.sequence_editor.services.CheckIfStillWritingService;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyHTMLEditor extends HTMLEditor {

    private ToggleButton boldButton, italicButton, underlineButton;
    private ColorPicker colorPicker;
    private WebEngine engine;
    private final IgnoreNumberingHtmlConverter htmlConverter;
    private final SequenceToHtmlConverter sequenceToHtmlConverter;
    private final CheckIfStillWritingService checkIfStillWritingService;
    private final Stack<String[]>[] changesLists;
    private String fontSize, lineLength;
    private String[] currentState;
    private boolean isTextSet;

    public MyHTMLEditor() {
        customizeHTMLEditor(this);
        this.checkIfStillWritingService = new CheckIfStillWritingService(this);
        this.changesLists = new Stack[] {new Stack<>(), new Stack<>()};
        this.setOnKeyReleased(keyEvent -> {
            setOnWriting(keyEvent);
            setOnShortCut(keyEvent);
        });
        this.setDisable(true);
        this.htmlConverter = new IgnoreNumberingHtmlConverter();
        this.sequenceToHtmlConverter = new SequenceToHtmlConverter();
        this.isTextSet = false;
        this.fontSize = "9";
        this.lineLength = "";
    }

    public void insertSequence(String[] sequence) {
        String[] oldState = new String[3];
        if (isTextSet) {
            oldState[0] = normalizeHtml(this.getHtmlText());
            oldState[1] = this.fontSize;
            oldState[2] = this.lineLength;
        }
        this.lineLength = String.valueOf(getLineLengthFromSequence(sequence));
        this.setHtmlText(sequenceToHTML(sequence, this.fontSize));
        if (isTextSet) {
            String[] newState = {this.getHtmlText(), this.fontSize, this.lineLength};
            saveIfChanges(oldState, newState, true);
        } else {
            this.isTextSet = true;
            this.currentState = new String[] {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        }
    }

    public String[] getNewState() {
        return new String[] {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
    }

    public String[] getCurrentState() {
        return this.currentState;
    }

    public String getNormalizedHtml() {
        return normalizeHtml(this.getHtmlText());
    }

    public void setToBold() {
        String[] oldState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        this.boldButton.fire();
        ignoreChangesOutsideOfSequence();
        String[] newState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        saveIfChanges(oldState, newState, true);
    }

    public void setToItalic() {
        String[] oldState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        this.italicButton.fire();
        ignoreChangesOutsideOfSequence();
        String[] newState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        saveIfChanges(oldState, newState, true);
    }

    public void setToUnderline() {
        String[] oldState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        this.underlineButton.fire();
        ignoreChangesOutsideOfSequence();
        String[] newState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        saveIfChanges(oldState, newState, true);
    }

    public void setColor(Color color, ActionEvent event) {
        String[] oldState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        this.colorPicker.setValue(color);
        this.colorPicker.fireEvent(event);
        ignoreChangesOutsideOfSequence();
        String[] newState = {normalizeHtml(this.getHtmlText()), this.fontSize, this.lineLength};
        saveIfChanges(oldState, newState, true);
    }

    public void setFontSize(String fontSize) {
        if (fontSize.equals(this.fontSize)) {
            return;
        }
        saveChanges(true);
        String currentHtml = this.getHtmlText();
        float scrollPosition = getScroll();
        String newHtml = currentHtml.replaceAll("font-size: \\d*", "font-size: " + fontSize);
        this.setHtmlText(newHtml);
        setScroll(scrollPosition);
        this.fontSize = fontSize;
    }

    public void changeLineLength(int newLineLength) {
        if (String.valueOf(newLineLength).equals(this.lineLength)) {
            return;
        }
        ChangeLineLengthService service = new ChangeLineLengthService(
                this.getHtmlText().replaceAll("<br>", "<br />"), newLineLength, this);
        service.startService();
    }

    public void setLineLength(String newLineLength) {
        this.lineLength = newLineLength;
    }

    public String getFontSize() {
        return this.fontSize;
    }

    public String getLineLength() {
        return this.lineLength;
    }

    public void undo() {
        this.checkIfStillWritingService.stopService();
        if (this.changesLists[0].size() > 0) {
            saveUndoneChanges();
            float scrollPosition = getScroll();
            String[] previousState = this.changesLists[0].pop();
            this.fontSize = previousState[1];
            this.lineLength = previousState[2];
            this.setHtmlText(previousState[0]);
            setScroll(scrollPosition);
            this.currentState = previousState;
        }
    }

    public void redo() {
        if (this.changesLists[1].size() > 0) {
            saveChanges(false);
            float scrollPosition = getScroll();
            String[] undoneState = this.changesLists[1].pop();
            this.fontSize = undoneState[1];
            this.lineLength = undoneState[2];
            this.setHtmlText(undoneState[0]);
            setScroll(scrollPosition);
            this.currentState = undoneState;
        }
    }

    public void setScroll(float relativePosition) {
        final WebEngine engine = this.engine;
        Platform.runLater(() -> {
            float documentHeight = (Integer) engine.executeScript("document.body.scrollHeight");
            float position = relativePosition * documentHeight;
            engine.executeScript("window.scrollTo(0, " + position + ")");
        });
    }

    public float getScroll() {
        float scrollPosition = (Integer) this.engine.executeScript("document.body.scrollTop");
        float documentHeight = (Integer) this.engine.executeScript("document.body.scrollHeight");
        return scrollPosition/documentHeight;
    }

    public void saveChanges(boolean resetUndoneChanges) {
        this.checkIfStillWritingService.stopService();
        if (resetUndoneChanges) {
            this.changesLists[1].clear();
        }
        if (this.changesLists[0].size() == 100) {
            this.changesLists[0].removeElementAt(0);
        }
        String[] state = {this.getHtmlText(), this.fontSize, this.lineLength};
        this.changesLists[0].push(state);
    }

    public void saveIfChanges(String[] oldState, String[] newState, boolean stopService) {
        if (stopService) {
            this.checkIfStillWritingService.stopService();
        }
        if (Arrays.equals(oldState, newState)) {
            return;
        }
        this.changesLists[1].clear();
        if (this.changesLists[0].size() == 100) {
            this.changesLists[0].removeElementAt(0);
        }
        this.changesLists[0].push(oldState);
        this.currentState = newState;
    }

    private void saveUndoneChanges() {
        String[] state = {this.getHtmlText(), this.fontSize, this.lineLength};
        this.changesLists[1].push(state);
    }

    private void ignoreChangesOutsideOfSequence() {
        float scrollPosition = getScroll();
        String newHtml = htmlConverter.ignoreChangesOutsideOfSequence(getNormalizedHtml());
        this.setHtmlText(newHtml);
        setScroll(scrollPosition);
    }

    private String sequenceToHTML(String[] sequence, String fontSize) {
        return this.sequenceToHtmlConverter.convert(sequence, fontSize);
    }

    private String normalizeHtml(String html) {
        return this.htmlConverter.normalizeHtmlFormat(html);
    }

    private int getLineLengthFromSequence(String[] sequence) {
        Pattern pattern = Pattern.compile("^([atgc]+) base pairs$", Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        for (String s: sequence) {
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                return matcher.group(1).length();
            }
        }
        return 0;
    }

    private void setOnWriting(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        KeyCombination redoCombo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
        if (code.isArrowKey() || redoCombo.match(keyEvent)) {
            return;
        }
        if (code == KeyCode.ENTER || code == KeyCode.BACK_SPACE || code == KeyCode.DELETE || code == KeyCode.TAB) {
            saveIfChanges(getCurrentState(), getNewState(), true);
        } else if (code.getCode() > 31 && code.getCode() < 127 || code.getCode() == 222) {
            this.changesLists[1].clear();
            this.checkIfStillWritingService.startService();
        }
    }

    private void setOnShortCut(KeyEvent keyEvent) {
        KeyCombination boldCombo = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
        KeyCombination italicCombo = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);
        KeyCombination underlineCombo = new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN);
        if (boldCombo.match(keyEvent) || italicCombo.match(keyEvent) || underlineCombo.match(keyEvent)) {
            ignoreChangesOutsideOfSequence();
        }
    }

    private void customizeHTMLEditor(final HTMLEditor editor) {
        hideHTMLEditorToolbars(editor);
        getToolbarChildrenAndSetGrowPriority(editor);
    }

    private void hideHTMLEditorToolbars(final HTMLEditor editor) {
        Platform.runLater(() -> {
            Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
            for(Node node : nodes) {
                node.setVisible(false);
                node.setManaged(false);
            }
        });
    }

    private void getToolbarChildrenAndSetGrowPriority(HTMLEditor editor) {
        Platform.runLater(() -> {
            Node[] nodes = {
                    editor.lookup(".html-editor-bold"),
                    editor.lookup(".html-editor-italic"),
                    editor.lookup(".html-editor-underline"),
                    editor.lookup(".html-editor-foreground"),
                    editor.lookup("WebView")};
            setBoldButton(nodes[0]);
            setItalicButton(nodes[1]);
            setUnderlineButton(nodes[2]);
            setColorPicker(nodes[3]);
            setWebViewAndEngine(nodes[4]);
            setWebViewGrowPriority(nodes[4]);
        });
    }

    private void setWebViewGrowPriority(Node webView) {
        GridPane.setHgrow(webView, Priority.ALWAYS);
        GridPane.setVgrow(webView, Priority.ALWAYS);
    }

    private void setBoldButton(Node node) {
        this.boldButton = (ToggleButton) node;
    }

    private void setItalicButton(Node node) {
        this.italicButton = (ToggleButton) node;
    }

    private void setUnderlineButton(Node node) {
        this.underlineButton = (ToggleButton) node;
    }

    private void setColorPicker(Node node) {
        this.colorPicker = (ColorPicker) node;
    }

    private void setWebViewAndEngine(Node node) {
        WebView webView = (WebView) node;
        this.engine = webView.getEngine();
    }
}