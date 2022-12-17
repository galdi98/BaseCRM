import java.sql.*;
import java.util.*;
import java.io.IOException;

public class BaseCRM implements AutoCloseable {

    // Default connection information (most can be overridden with command-line arguments)
    private static final String DB_NAME = "fg0103_BaseCRM";
    private static final String DB_USER = "token_cdc3";
    private static final String DB_PASSWORD = "CgnuidQFjDsf6rvV";

    // Connection information to use
    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser, dbPassword;

    // The database connection and prepared statement (query)
    private Connection connection;
    
    public BaseCRM(String dbHost, int dbPort, String dbName,
            String dbUser, String dbPassword) throws SQLException {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;

        connect();
    }

    private void connect() throws SQLException {
        // URL for connecting to the database: includes host, port, database name,
        // user, password
        final String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                dbHost, dbPort, dbName,
                dbUser, dbPassword
        );

        // Attempt to connect, returning a Connection object if successful
        this.connection = DriverManager.getConnection(url);
    }

    public void runApp() throws SQLException {
        // You may not have seen the Console class before, but it has one
        // advantage over a Scanner: it has the readPassword() method,
        // which does not echo the password as the user types it.
        Scanner in = new Scanner (System.in);
        System.out.print("\n-------------------------\n"
        		+"BaseCRM By Francesco Galdiolo\n"
        		+ "HOME PAGE\n"
        		+ "-------------------------\n"
        		+ "What do you want to do?\n"
        		+ "\n - 1 for insert"
        		+ "\n - 2 for delete"
        		+ "\n - 3 for show data\n"
        		+ " - 0 for QUIT.\n");
        int  main = in.nextInt();
        
        if(main==0)
        	System.exit(0);
//ADD
        if(main==1) {
        System.out.print("What do you want to add?"
        		+ "\n- 1 for New Ticket"
        		+ "\n- 2 for New Email"
        		+ "\n- 3 for New Phone Call"
        		+ "\n- 4 for New Client\n"
        		+ " - 0 for QUIT\n"
        		+ " - 9 for main menu");
        int addChoice = in.nextInt();
        
        if(addChoice==0)
        	System.exit(0);
        if(addChoice==9)
        	runApp();
        
         //TICKET
        if(addChoice==1) {
        	final String ADD_TICKET_QUERY = 
                    "INSERT INTO Ticket (date, subject, `desc`, priority, status, clientEIN, employeeId)\n"
                    + "VALUES (default, ?, ?, ?, ?, ?, ?)";
        var statement = connection.prepareStatement(ADD_TICKET_QUERY);
        
        System.out.print("Subject:");
        in.nextLine();
        String subject = in.nextLine();
        System.out.print("Description:");
        String description = in.nextLine();
        System.out.print("Priority (low-medium-high):");
        String priority = in.nextLine();
        System.out.print("Status (PE-AS-OP-RE):");
        String status = in.nextLine();
        System.out.print("Client EIN:");
        String clientEIN = in.nextLine();
        System.out.print("Employee ID:");
        int employeeId = in.nextInt();
        
            statement.setString(1, subject);
            statement.setString(2, description);
            statement.setString(3, priority);
            statement.setString(4, status);
            statement.setString(5, clientEIN);
            statement.setInt(6, employeeId);
            statement.execute();
            System.out.println("Item added.");
            runApp();
        	}
 //EMAIL  
        if(addChoice==2) {
        	final String ADD_EMAIL_QUERY = 
                    "INSERT INTO Email (date, clientEIN, subject, text, employeeId)\n"
                    + "VALUES (default, ?, ?, ?, ?)";
        	
        var statement = connection.prepareStatement(ADD_EMAIL_QUERY);
            System.out.print("ClientEIN:");
            in.nextLine();
            String clientEIN = in.nextLine();
            System.out.print("Subject:");
            String subject = in.nextLine();
            System.out.print("Text:");
            String text = in.nextLine();
            System.out.print("employeeId:");
            int employeeId = in.nextInt();
            
                statement.setString(1, clientEIN);
                statement.setString(2, subject);
                statement.setString(3, text);
                statement.setInt(4, employeeId);
                statement.execute();
                System.out.println("Item added.");
                runApp();
        	}
 //PHONECALL
        if(addChoice==3) {
        	final String ADD_PHONECALL_QUERY = 
                    "INSERT INTO PhoneCall (date, clientEIN, subject, employeeId)\n"
                    + "VALUES (default, ?, ?, ?)";
        	
        var statement = connection.prepareStatement(ADD_PHONECALL_QUERY);
            
            System.out.print("ClientEEN:");
            in.nextLine();
            String clientEIN = in.nextLine();
            System.out.print("Subject:");
            String subject = in.nextLine();
            System.out.print("employeeId:");
            String employeeId = in.nextLine();
            
                statement.setString(1, clientEIN);
                statement.setString(2, subject);
                statement.setString(3, employeeId);
                statement.execute();
                System.out.println("Item added.");
        	}
    //CLIENT
        if(addChoice==4) {
        	final String ADD_CLIENT_QUERY = 
                    "INSERT INTO Client (EIN, location, assistanceContract, clientSince)\n"
                    + "VALUES (?, ?, ?, ?)";
        	
        var statement = connection.prepareStatement(ADD_CLIENT_QUERY);
            
            System.out.println("Client EIN:");
            String EIN = in.nextLine();
            System.out.println("Location/Address:");
            String address = in.nextLine();
            System.out.println("Assistance Contract file name:");
            String fileName = in.nextLine();
            System.out.println("Client since (YYYY-MM-DD):");
            String clientSince = in.nextLine();
            
                statement.setString(1, EIN);
                statement.setString(2, address);
                statement.setString(3, fileName);
                statement.setString(4, clientSince);
                statement.execute();
                System.out.println("Item added.");
                runApp();
        	}
        }
        
        
 //DELETE
        if(main==2) {
            System.out.print("What do you want to delete?"
            		+ "\n- 1 for delete Ticket"
            		+ "\n- 2 for delete Email"
            		+ "\n- 3 for delete Phone Call"
            		+ "\n- 4 for delete Client\n"
            		+ " - 0 for QUIT"
            		+ "\n - 9 for main menu");
            int removeChoice = in.nextInt();
            
            if(removeChoice==0) 
            	System.exit(0);
            if(removeChoice==9) 
            	runApp();
            
     //TICKET      
            if(removeChoice==1) {
            	final String DELETE_TICKET_QUERY = 
                        "DELETE FROM Ticket\n"
                        + "WHERE id = ?\n";
                
                var statement = connection.prepareStatement(DELETE_TICKET_QUERY);
            	System.out.print("Delete by id.\n"
            			+ "Search id here:");
            		int ans = in.nextInt();
            		statement.setInt(1, ans);
            		statement.execute();
            		System.out.println("Item deleted");
            		runApp();
            	}
            
        //EMAIL
            if(removeChoice==2) {
            	final String DELETE_EMAIL_QUERY = 
                        "DELETE FROM Email\n"
                       + "WHERE clientEIN = ?\n"
                       + "and subject LIKE ?";
                
                var statement = connection.prepareStatement(DELETE_EMAIL_QUERY);
            System.out.println("Delete by Client and Subject\n");
            System.out.print("What's the EIN of the client that sent the email?:");
            in.nextLine();
        	String ans = in.nextLine();
        	System.out.print("What was the subject of the email?:");
        	String ans1 = in.nextLine();
        	statement.setString(1, ans);
        	statement.setString(2, ans1);
            statement.execute();
            System.out.println("Item deleted");
            runApp();
            }
            if(removeChoice==3) {
            	final String DELETE_PHONECALL_QUERY = 
                        "DELETE FROM PhoneCall\n"
                        + "WHERE clientEIN = ?\n"
                        + "and subject LIKE ?";
                
                var statement = connection.prepareStatement(DELETE_PHONECALL_QUERY);
                System.out.print("What's the EIN of the client that called?:");
                in.nextLine();
            	String ans = in.nextLine();
            	System.out.print("What was the subject of the call?:");
            	String ans1 = in.nextLine();
            	statement.setString(1, ans);
            	statement.setString(2, ans1);
                statement.execute();
                System.out.println("Item deleted");
                runApp();
            }
  //CLIENT              
            if(removeChoice==4) {
            	final String DELETE_CLIENT_QUERY = 
                        "DELETE FROM Client\n"
                        + "WHERE EIN = ?\n";
                
                var statement = connection.prepareStatement(DELETE_CLIENT_QUERY);
            	System.out.print("What's the EIN of the client you want to delete?:");
            	String ans = in.nextLine();
            	statement.setString(1, ans);
                statement.execute();
                System.out.println("Item deleted");
                runApp();
            	}
        }
    //SHOW
        if(main==3) {
        	
        	System.out.print("What do you want to show?"
            		+ "\n - 1 for all Tickets"
            		+ "\n - 2 for all Emails"
            		+ "\n - 3 for all Phone Calls"
            		+ "\n - 4 for all Clients\n"
            		+ " - 0 for QUIT"
            		+ "\n - 9 for main menu");
        	int showChoice = in.nextInt();
        	
            if(showChoice==0) 
            	System.exit(0);
            if(showChoice==9)
            	runApp();
            
        	
            if(showChoice==1)  {   
            	final String TICKET_QUERY =
                        "SELECT id, date, subject, priority, status, clientEIN, employeeId\n"
                        + "FROM Ticket\n";
                    var statement = connection.prepareStatement(TICKET_QUERY);
            var results = statement.executeQuery();
            
            System.out.println("\nTickets:");
            
            while (results.next()) {
                var id = results.getInt("id");
                var date = results.getString("date");
                var subject = results.getString("subject");
                var priority = results.getString("priority");
                var status = results.getString("status");
                var clientEIN = results.getString("clientEIN");
                System.out.printf("%d - %s - %.20s - %s - %s - %s \n", id, date, subject, priority, status, clientEIN);
            	}
            runApp();
            }
            if(showChoice==2)  {   
            	final String EMAIL_QUERY =
                        "SELECT date, clientEIN, subject, text, employeeId\n"
                        + "FROM Email\n";
                    var statement = connection.prepareStatement(EMAIL_QUERY);
            var results = statement.executeQuery();
            
            System.out.println("\nEmails:");
            
            while (results.next()) {
                var date = results.getString("date");
                var clientEIN = results.getString("clientEIN");
                var subject = results.getString("subject");
                var text = results.getString("text");
                var employeeId = results.getString("employeeId");
                System.out.printf("%s - %s - %s - %s - %s \n", date, clientEIN, subject, text, employeeId);
            	}
            runApp();
            }
            if(showChoice==3)  {   
            	final String PHONECALL_QUERY =
                        "SELECT date, clientEIN, subject, text, employeeId\n"
                        + "FROM Email\n";
                    var statement = connection.prepareStatement(PHONECALL_QUERY);
            var results = statement.executeQuery();
            
            System.out.println("\nPhone Calls:");
            
            while (results.next()) {
                var date = results.getString("date");
                var clientEIN = results.getString("clientEIN");
                var subject = results.getString("subject");
                var employeeId = results.getString("employeeId");
                System.out.printf("%s - %s - %s - %s \n", date, clientEIN, subject, employeeId);
            	}
            runApp();
            }
            if(showChoice==4)  {   
            	final String CLIENT_QUERY =
                        "SELECT EIN, location, clientSince\n"
                        + "FROM Client\n";
                    var statement = connection.prepareStatement(CLIENT_QUERY);
            var results = statement.executeQuery();
            
            System.out.println("\nClients:");
            
            while (results.next()) {
                var EIN = results.getString("EIN");
                var location = results.getString("location");
                var clientSince = results.getString("clientSince");
                System.out.printf("%s - %s - %s\n", EIN, location, clientSince);
            	}
            runApp();
            }
          }
        }
    

    /**
     * Closes the connection to the database.
     */
    @Override
    public void close() throws SQLException {
        connection.close();
    }
    
    

    /**
     * Entry point of the application. Uses command-line parameters to override database
     * connection settings, then invokes runApp().
     */
    public static void main(String... args) {
        // Default connection parameters (can be overridden on command line)
        Map<String, String> params = new HashMap<>(Map.of(
            "dbname", "" + DB_NAME,
            "user", DB_USER,
            "password", DB_PASSWORD
        ));

        boolean printHelp = false;

        // Parse command-line arguments, overriding values in params
        for (int i = 0; i < args.length && !printHelp; ++i) {
            String arg = args[i];
            boolean isLast = (i + 1 == args.length);

            switch (arg) {
            case "-h":
            case "-help":
                printHelp = true;
                break;

            case "-dbname":
            case "-user":
            case "-password":
                if (isLast)
                    printHelp = true;
                else
                    params.put(arg.substring(1), args[++i]);
                break;

            default:
                System.err.println("Unrecognized option: " + arg);
                printHelp = true;
            }
        }

        // If help was requested, print it and exit
        if (printHelp) {
            printHelp();
            return;
        }

        // Connect to the database. This use of "try" ensures that the database connection
        // is closed, even if an exception occurs while running the app.
        try (DatabaseTunnel tunnel = new DatabaseTunnel();
             var app = new BaseCRM(
                "localhost", tunnel.getForwardedPort(), params.get("dbname"),
                params.get("user"), params.get("password")
            )) {
            // Run the interactive mode of the application.
            app.runApp();
        } catch (IOException ex) {
            System.err.println("Error setting up ssh tunnel.");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Error communicating with the database (see full message below).");
            ex.printStackTrace();
            System.err.println("\nParameters used to connect to the database:");
            System.err.printf("\tSSH keyfile: %s\n\tDatabase name: %s\n\tUser: %s\n\tPassword: %s\n\n",
                    params.get("sshkeyfile"), params.get("dbname"),
                    params.get("user"), params.get("password")
            );
            System.err.println("(Is the MySQL connector .jar in the CLASSPATH?)");
            System.err.println("(Are the username and password correct?)");
        }
        
    }

    private static void printHelp() {
        System.out.println("Accepted command-line arguments:");
        System.out.println();
        System.out.println("\t-help, -h          display this help text");
        System.out.println("\t-dbname <text>     override name of database to connect to");
        System.out.printf( "\t                   (default: %s)\n", DB_NAME);
        System.out.println("\t-user <text>       override database user");
        System.out.printf( "\t                   (default: %s)\n", DB_USER);
        System.out.println("\t-password <text>   override database password");
        System.out.println();
    }
}
