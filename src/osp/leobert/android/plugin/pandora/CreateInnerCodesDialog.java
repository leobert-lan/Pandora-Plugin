package osp.leobert.android.plugin.pandora;

import javax.swing.*;
import java.awt.event.*;

public class CreateInnerCodesDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField typeName;
    private JCheckBox onlyItemViewBinderCheckBox;


    public CreateInnerCodesDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private OnOKListener onOKListener;


    public interface OnOKListener {
        void onOK(String text, boolean and);
    }


    public void setOnOKListener(OnOKListener onOKListener) {
        this.onOKListener = onOKListener;
    }


    public OnOKListener getOnOKListener() {
        return onOKListener;
    }


    private void onOK() {
        if (onOKListener != null) {
            onOKListener.onOK(typeName.getText(), onlyItemViewBinderCheckBox.isSelected());
        }
        dispose();
    }


    private void onCancel() {
        dispose();
    }


    public static void main(String[] args) {
        CreateInnerCodesDialog dialog = new CreateInnerCodesDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
