package com.codehousing.junitgui.gui;

import com.codehousing.example.test.ExampleSuite;
import com.codehousing.example.test.ThirdTest;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;

import java.net.URL;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;

public class MainGUI extends JPanel {
//    private JEditorPane htmlPane;
    private JTextArea logPane;
    private JTree tree;
    private URL helpURL;
    private final DefaultMutableTreeNode rootGuiNode;
    private final TestTreeNode[] rootNodes;
    private DefaultMutableTreeNode currentGuiNode;
    private TestTreeNode currentNode;
//    private BookInfo currentBook;
    private static boolean DEBUG = true;

    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";

    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;

    public MainGUI(TestTreeNode... nodes) {
        super(new GridLayout(1,0));

        this.rootNodes = nodes;
        //Create the nodes.
        rootGuiNode = new DefaultMutableTreeNode("All Tests");
        fillInChildren(rootGuiNode, Arrays.asList(nodes));

        //Create a tree that allows one selection at a time.
        tree = new JTree(rootGuiNode);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this::valueChanged);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);

                JButton button = new JButton("click");
                button.addActionListener(e -> {
                    System.out.println("himom");
                    JUnitCore junit = new JUnitCore();
                    junit.addListener(new TextListener(System.out));
                    junit.addListener(new TestRunListener());
                    if(currentGuiNode == rootGuiNode) {
                        for(TestTreeNode node : rootNodes) {
                            junit.run(node.getTestClass());
                        }
                    } else if(currentNode == null) {
                        System.out.println("No test selected");
                    } else if(currentNode.isLeaf()) {
                        junit.run(Request.method(currentNode.getTestClass(), currentNode.getTestMethod().getName()));
                    } else {
                        junit.run(currentNode.getTestClass());
                    }
//                    junit.run(ExampleTest.class);
//                    org.junit.runner.JUnitCore.runClasses(ExampleTest.class);
                    System.out.println("done with tests");
                });
        button.setBounds(130, 100, 100, 40);
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.PAGE_AXIS));
        actionPanel.add(treeView);
        actionPanel.add(button);

        logPane = new JTextArea();
        logPane.setEditable(false);
        JScrollPane htmlView = new JScrollPane(logPane);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(actionPanel);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);
    }

    private class TestRunListener extends RunListener {
        @Override
        public void testFailure(Failure failure) {
            log(failure.getMessage());
        }

        @Override
        public void testFinished(Description description) {
            log("Finish! " + description.getTestClass() + description.getMethodName());
        }
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
       currentGuiNode = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();

        if (currentGuiNode == null) return;

        Object nodeInfo = currentGuiNode.getUserObject();
        if(nodeInfo instanceof TestTreeNode) {
            currentNode = (TestTreeNode) nodeInfo;
            System.out.println("Node: " + currentNode.getDescription());
        }
//        if (node.isLeaf()) {
//            currentBook = (BookInfo)nodeInfo;
////            displayURL(currentBook.bookURL);
//            if (DEBUG) {
//                System.out.print(currentBook.bookURL + ":  \n    ");
//            }
//        } else {
////            displayURL(helpURL);
//        }
//        if (DEBUG) {
//            System.out.println(nodeInfo.toString());
//        }
    }

    private class BookInfo {
        public String bookName;
        public URL bookURL;

        public BookInfo(String book, String filename) {
            bookName = book;
            bookURL = getClass().getResource(filename);
            if (bookURL == null) {
                System.err.println("Couldn't find file: "
                        + filename);
            }
        }

        public String toString() {
            return bookName;
        }
    }

    private void fillInChildren(DefaultMutableTreeNode parent, Iterable<TestTreeNode> childNodes) {
        for(TestTreeNode childNode : childNodes) {
            DefaultMutableTreeNode guiNode = new DefaultMutableTreeNode(childNode);
            parent.add(guiNode);
            if(!childNode.isLeaf()) {
                fillInChildren(guiNode, childNode.getChildren());
            }
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new MainGUI(new TestTreeNode(ExampleSuite.class), new TestTreeNode(ThirdTest.class)));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void log(String message) {
        logPane.append(message);
        logPane.append("\n");
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("Couldn't use system look and feel.");
                }
                createAndShowGUI();
            }
        });
    }
}