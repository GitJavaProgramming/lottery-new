package org.pp;

import org.pp.filter.DefaultBuildConditionFilter;
import org.pp.task.CacheSelectionNumberTask;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-25
 * Time: 下午9:12
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }

        DefaultBuildConditionFilter.getInstance();

        SwingUtilities.invokeLater(() -> new MainFrame());

        CacheSelectionNumberTask.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("执行钩子，请检查文件是否已经写入完成等操作。")));
    }
}
