package crypto.session;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import crypto.utils.Util;
import crypto.users.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserSession 
{
    private static volatile UserSession instance;
    private User currentUser;
    private String sessionToken;
    private long sessionStartTime;
    private long lastActivityTime;
    private boolean isGuest;
    private boolean isAdmin;
    private Map<String, Object> sessionAttributes;
    private String sessionId;
    
    private UserSession() 
    {
        this.sessionAttributes = new HashMap<>();
        this.sessionId = UUID.randomUUID().toString();
    }
    
    public static UserSession getInstance() 
    {
        if(instance == null) 
        {
            synchronized (UserSession.class)
            {
                if(instance == null)
                    instance = new UserSession(); 
            }
        }
        return instance;
    }
    
    public void createUserSession(User user, boolean isGuest) 
    {
        this.currentUser      = user;
        this.isGuest          = isGuest;
        this.isAdmin          = false;
        this.sessionStartTime = System.currentTimeMillis();
        this.lastActivityTime = this.sessionStartTime;
        this.sessionToken     = generateSessionToken();
        this.sessionAttributes.clear();
        
        sessionAttributes.put("login_time", LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //sessionAttributes.put("ip_address", "127.0.0.1");
        sessionAttributes.put("user_agent", "Java Swing Client");
    }
    
    public void createGuestSession()   
    {
        this.currentUser      = new User("Guest", "guest@temp.com", "");
        this.isGuest          = true;
        this.isAdmin          = false;
        this.sessionStartTime = System.currentTimeMillis();
        this.lastActivityTime = this.sessionStartTime;
        this.sessionToken     = "GUEST_" + System.currentTimeMillis();
        this.sessionAttributes.clear();
        
        sessionAttributes.put("login_time", LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sessionAttributes.put("user_type", "guest");
    }
    
    public void destroySession() 
    {
        String username = (currentUser != null) ? currentUser.getLogin() : "Unknown";
        System.out.println("Session destroyed for user: " + username);
        
        if(currentUser != null && !isGuest) 
        {
            sessionAttributes.put("logout_time", LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sessionAttributes.put("session_duration", getSessionDuration());
        }
        
        this.currentUser   = null;
        this.sessionToken  = null;
        this.isGuest       = false;
        this.isAdmin       = false;
        this.sessionAttributes.clear();
        this.sessionId = UUID.randomUUID().toString();
    }
    
    public boolean isLoggedIn() 
    {
        return currentUser != null && isSessionValid();
    }
    
    public boolean isGuest() 
    {
        return isGuest;
    }
    
    public boolean isAdmin() 
    {
        return isAdmin;
    }
    
    public User getCurrentUser() 
    {
        return currentUser;
    }
    
    public String getSessionToken() 
    {
        return sessionToken;
    }
    
    public String getSessionId() 
    {
        return sessionId;
    }
    
    public long getSessionDuration() 
    {
        return System.currentTimeMillis() - sessionStartTime;
    }
    
    public long getInactivityDuration() 
    {
        return System.currentTimeMillis() - lastActivityTime;
    }
    
    public String getFormattedSessionDuration() 
    {
        return Util.formatDuration(getSessionDuration());
    }
    
    public String getFormattedInactivityDuration() 
    {
        return Util.formatDuration(getInactivityDuration());
    }
    
    private String generateSessionToken() 
    {
        return UUID.randomUUID().toString().replace("-", "") + 
               "_" + sessionStartTime;
    }
    
    public boolean isSessionValid() 
    {
        if(currentUser == null) return false;
        
        long currentTime        = System.currentTimeMillis();
        long sessionDuration    = currentTime - sessionStartTime;
        long inactivityDuration = currentTime - lastActivityTime;
        
        long sessionTimeout    = 8 * 60 * 60 * 1000;
        long inactivityTimeout = 30 * 60 * 1000; 
        
        return sessionDuration < sessionTimeout && inactivityDuration < inactivityTimeout;
    }
    
    public void updateActivity() 
    {
        if(isLoggedIn())
            this.lastActivityTime = System.currentTimeMillis();
    }
    
    public void refreshSession() 
    {
        if(isLoggedIn()) 
        {
            this.sessionStartTime = System.currentTimeMillis();
            this.lastActivityTime = this.sessionStartTime;
        }
    }

    public boolean hasPermission(String permission) 
    {
        if(isAdmin) 
            return true;
        
        if(isGuest) 
        {
            return permission.equals("basic_access") || 
                   permission.equals("view_home");
        } 

        else 
        {
            return permission.equals("basic_access") ||
                   permission.equals("view_home") ||
                   permission.equals("cryptanalysis") ||
                   permission.equals("save_results");
        }
    }
    
    public void setAttribute(String key, Object value) 
    {
        sessionAttributes.put(key, value);
    }
    
    public Object getAttribute(String key) 
    {
        return sessionAttributes.get(key);
    }
    
    public Map<String, Object> getSessionInfo() 
    {
        Map<String, Object> info = new HashMap<>(sessionAttributes);
        info.put("session_id", sessionId);
        info.put("user_type", isGuest ? "guest" : "registered");
        info.put("login", currentUser != null ? currentUser.getLogin() : "none");
        info.put("session_start", sessionStartTime);
        info.put("last_activity", lastActivityTime);
        return info;
    }
    
    public String getSessionSummary() 
    {
        if(!isLoggedIn()) return "Aucune session active";
        
        String userType   = isGuest ? "Invité" : "Utilisateur";
        String userName   = currentUser.getLogin();
        String duration   = getFormattedSessionDuration();
        String inactivity = getFormattedInactivityDuration();
        
        return String.format(
            "Type: %s | Utilisateur: %s | Durée: %s | Inactivité: %s",
            userType, userName, duration, inactivity
        );
    }
}
