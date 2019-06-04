package osp.leobert.android.plugin.pandora;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class CreateKtCodesDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField typeName;
    private JCheckBox onlyItemViewBinderCheckBox;
    private JRadioButton kotlinRadioButton;
    private JRadioButton useKotlinAndKtPandoraRadioButton;

    private Integer type = 1;


    public CreateKtCodesDialog() {
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
        kotlinRadioButton.addActionListener(e -> type = 1);
        useKotlinAndKtPandoraRadioButton.addActionListener(
                e-> type = 2
        );

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        kotlinRadioButton.setSelected(true);
        contentPane.requestFocus();
    }


    private OnOKListener onOKListener;



    public interface OnOKListener {
        void onOK(String text, boolean and, int type);
    }


    public void setOnOKListener(OnOKListener onOKListener) {
        this.onOKListener = onOKListener;
    }


    public OnOKListener getOnOKListener() {
        return onOKListener;
    }


    private void onOK() {
        if (onOKListener != null) {
            onOKListener.onOK(typeName.getText(), onlyItemViewBinderCheckBox.isSelected(),type);
        }
        dispose();
    }


    private void onCancel() {
        dispose();
    }


    public static void main(String[] args) {
        CreateKtCodesDialog dialog = new CreateKtCodesDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
