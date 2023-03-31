import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zeynep Selcen Öztunç
 * ID: 21902941
 */

public class ProxyDownloader {
   
    public static void downloadFile(String url) throws IOException {
        
        // create a new connection with the url in the method argument
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");//with get request method

        //get the response code from connection
        int response = connection.getResponseCode();
        
        // if the response message is OK
        if (response == HttpURLConnection.HTTP_OK) {

            // get filename 
            String filename = url.substring(url.lastIndexOf("/") + 1);

            //array to store the read data
            byte[] buffer = new byte[4096];
            int numOfBytes = -1;

            // create an output stream with the filename
            FileOutputStream oStream = new FileOutputStream(filename);

            //read the input stream
            InputStream iStream = connection.getInputStream();// get the input stream
            while ((numOfBytes = iStream.read(buffer)) != -1) {
                oStream.write(buffer, 0, numOfBytes);//write it to the output stream
            }
            oStream.close();
        
            //finds all the hyperlinks in the response
            String responseMessage = new String(buffer, StandardCharsets.UTF_8);
            Pattern hyperLink = Pattern.compile("<a\\s+[^>]*href=\"([^\"]*)\"[^>]*>");
            Matcher match = hyperLink.matcher(responseMessage);

            //while there are still hyperlinks
            while (match.find()) {
                String hLink = match.group(1);

                // create an url object
                URL absoluteUrl = new URL(url);
                URL linkUrl = new URL(absoluteUrl, hLink);//new url object with absolute url as base
                String downloadUrl = linkUrl.toString();

                downloadFile(downloadUrl);//recursively call the method
            }
            System.out.println("Saving file...");
        } 
        else {
            // print an error message if the response code is not 200
            System.out.println("Error, could not download the files!: " + response 
            + " " + connection.getResponseMessage());
        }
    }

    public static void sendResponse(Socket clientSocket, URLConnection connection,
    String url) throws IOException {

        //init the content length and type
        int cLength = connection.getContentLength();
        String cType = connection.getContentType();

        //init the input and output streams
        InputStream inputStream = connection.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);

        //the http response message
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: " + cType);//this line is necessary
        out.println("Content-Length: " + cLength);
        out.println();

        byte[] buffer = new byte[1024];
        int numOfBytes;

        //display the output in the browser
        while ((numOfBytes = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, numOfBytes);
        }
        outputStream.close();
    }
    
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket;
        Socket clientSocket;

        //give an error if the command line arguments are wrong
        if (args.length != 1) {
            System.out.println("You should enter the following way: java ProxyDownloader <port>");
            return;
        }

        //get the port number from the terminal
        int port = Integer.parseInt(args[0]);

        //create server socket
        serverSocket=new ServerSocket(port);

        while(true){
            clientSocket=serverSocket.accept();//a socket object representing client's connection

            BufferedReader clientReq= new BufferedReader(new InputStreamReader
            (clientSocket.getInputStream()));
            String line;//lines of the HTTP request message

            StringBuilder msgBuilder = new StringBuilder();
            String clientRequest="";
            int i = 0;
            //build the http get message
            while((line=clientReq.readLine()) != null && !line.isEmpty()){
                if(i == 0){
                    clientRequest= line;//first line is the request from the client
                }
                msgBuilder.append(line).append("\n");
                i++;
            }

            //divide the request into parts
            String requestParts[]=clientRequest.split(" ");
            String url= requestParts[1];

            //only print the bilkent server requests/responses
            if(!url.contains("www.cs.bilkent.edu.tr")){
                System.out.println();
                continue;
            }

            //print the get request
            String requestMessage = msgBuilder.toString();
            System.out.println("Retrieved request from Firefox :\n");
            System.out.println(requestMessage);

            //get the filename
            String filename=url.substring(url.lastIndexOf("/") + 1);
  
            //if the filename is null, continue
            if(filename == null||filename.equals(" ")){
                System.out.println("Invalid file name!" + filename);
                System.out.println();
                continue;
            }

            //check if the filename ends with .txt 
            if(!filename.contains(".txt")){
                System.out.println("Not a text file... ");
                System.out.println("You can try connecting to a new website. ");
                System.out.println();
                continue;
            }
            else{
                System.out.println("Downloading file  '" + filename +"'...");
            }

            //create an URL object to open a connection
            URL server= new URL(url);
            HttpURLConnection connection= (HttpURLConnection)server.openConnection();//connection object
            connection.setRequestMethod("GET");

            //get the HTTP status code
            int status= connection.getResponseCode();

            //if the status code is ok
            if(status == HttpURLConnection.HTTP_OK){

                //if the url is valid, send response to the client socket
                if(url != null){
                    sendResponse(clientSocket, connection, url);
                }

                //print the HTTP status code and message
                System.out.println("Retrieved "+ status +" " +connection.getResponseMessage());

                //download the files
                downloadFile(url);
                System.out.println();
                System.out.println();
                
            }
            //if the status message is not OK
            else{
                System.out.println("ERROR! "+ status +" " +connection.getResponseMessage());
            }

        }

    }
    
}
