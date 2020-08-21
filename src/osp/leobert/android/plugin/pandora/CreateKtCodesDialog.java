package osp.leobert.android.plugin.pandora;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateKtCodesDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField typeName;
    private JCheckBox onlyItemViewBinderCheckBox;
    private JRadioButton kotlinRadioButton;
    private JRadioButton useKotlinAndKtPandoraRadioButton;
    private JCheckBox reactiveCheckBox;
    private JButton buttonRefresh;

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
                e -> type = 2
        );

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        kotlinRadioButton.setSelected(true);

        reactiveCheckBox.setSelected(true);
        contentPane.requestFocus();

        buttonRefresh.addActionListener(e -> {
            if (onRefreshListener != null)
                onRefreshListener.actionPerformed(e);
        });
    }


    private OnOKListener onOKListener;

    private ActionListener onRefreshListener;


    public interface OnOKListener {
        void onOK(String text, boolean and, int type, boolean reactive);
    }


    public void setOnOKListener(OnOKListener onOKListener) {
        this.onOKListener = onOKListener;
    }

    public void setOnRefreshListener(ActionListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public OnOKListener getOnOKListener() {
        return onOKListener;
    }


    private void onOK() {
        if (onOKListener != null) {
            onOKListener.onOK(typeName.getText(), onlyItemViewBinderCheckBox.isSelected(), type,
                    reactiveCheckBox.isSelected());
        }
        dispose();
    }


    private void onCancel() {
        dispose();
    }

    /**
     * invoke model and config refresh
     */
    private void onRefreshClicked() {

    }


    public static void main(String[] args) {
        CreateKtCodesDialog dialog = new CreateKtCodesDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
