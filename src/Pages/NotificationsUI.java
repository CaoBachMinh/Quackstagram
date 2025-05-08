package src.Pages;

import src.DataManager.DataManager;
import src.DataManager.NotificationManager;
import src.Components.Query.NotificationQuery;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class NotificationsUI extends UIManager {
    private DataManager notificationManager;
    
    public NotificationsUI() {
        setTitle("Notifications");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }
    
    private void initializeUI() {
        JPanel headerPanel = createHeaderPanel(" Notifications 🐥");
        JPanel navigationPanel = createNavigationPanel();
        JPanel contentPanel = createNotificationContentPanel();
        JScrollPane scrollPane = createScrollPane(contentPanel);
        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private JPanel createNotificationContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    
        // Add notifications to the content panel
        addNotificationsToPanel(contentPanel);
        return contentPanel;
    }

    private void addNotificationsToPanel(JPanel contentPanel) {
        readNotificationFile();
        NotificationQuery notificationData = new NotificationQuery();
        List<String> notifications = notificationData.getNotificationMessages();

        for (String notification : notifications) {
            JPanel notificationPanel = new JPanel(new BorderLayout());
            notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            JLabel notificationLabel = new JLabel(notification);
            notificationPanel.add(notificationLabel, BorderLayout.CENTER);
            // Add profile icon (if available) and timestamp
            // ... (Additional UI components if needed)
            contentPanel.add(notificationPanel);
        }
    }

    private JScrollPane createScrollPane(JPanel contentPanel){
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private void readNotificationFile(){
        notificationManager = new NotificationManager();
        notificationManager.readDatabase();
    }
}
