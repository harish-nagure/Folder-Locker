import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class FolderLockerApp 
{

    public static void main(String[] args) 
    {
        String folderPath,password,answer;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Folder Locker Application");
        System.out.println("=========================");

        // Get folder path from the user
        System.out.print("Enter the path of the folder to lock: ");
        folderPath = scanner.nextLine();

        // Check if the folder exists
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) 
        {
            System.out.println("Invalid folder path or folder does not exist.");
            return;
        }

        // Get a password from the user to lock the folder
        System.out.print("Enter a password to lock the folder: ");
        password = scanner.nextLine();

        // Encrypt the folder with the given password
        if (lockFolder(folder, password)) 
        {
            System.out.println("Folder locked successfully!");
        } 
        else 
        {
            System.out.println("Failed to lock the folder. Please try again.");
        }
        scanner.close();
    }

    private static boolean lockFolder(File folder, String password) 
    {
        try {

            // Create a temporary folder to store the encrypted files
            File tempFolder = new File(folder.getParent(), folder.getName() + "_temp");
            if (!tempFolder.exists()) 
            {
                tempFolder.mkdir();
            }
            
             System.out.print("Folder locked! Do you want to unlock it later (Yes/No)? ");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();

            File[] files = folder.listFiles();
            if (files != null) 
            {
                for (File file : files) 
                {
                    if (file.isFile()) 
                    {
                        if (answer.equalsIgnoreCase("Yes") || answer.equalsIgnoreCase("Y"))
                        {
                        byte[] fileContent = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                        byte[] encryptedContent = decrypt(fileContent, password);
                        Files.write(Paths.get(tempFolder.getAbsolutePath(), file.getName()), encryptedContent);
                        }
                        else
                        {
                        byte[] fileContent = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                        byte[] encryptedContent = encrypt(fileContent, password);
                        Files.write(Paths.get(tempFolder.getAbsolutePath(), file.getName()), encryptedContent);
                        }
                    }
                }
            }

            // Remove the original folder and rename the temporary folder
            deleteFolder(folder);
            tempFolder.renameTo(folder);

            return true;
        } 
        catch (Exception e) 
        {
            System.out.println("caught: "+e);
            return false;
        }
    }

    private static byte[] encrypt(byte[] data, String password) 
    {
        byte[] encryptedData = new byte[data.length];
        int passwordInt = Integer.parseInt(password);

        for (int i = 0; i < data.length; i++) 
        {
            //System.out.println(data[i]+"  "+passwordInt);
            encryptedData[i] = (byte) (data[i]+(int)passwordInt);
        }
        return encryptedData;
    }
   private static byte[] decrypt(byte[] data, String password) 
    {
        byte[] encryptedData = new byte[data.length];
        int passwordInt = Integer.parseInt(password);

        for (int i = 0; i < data.length; i++) 
        {
            //System.out.println(data[i]+"  "+passwordInt);
            encryptedData[i] = (byte) (data[i]-(int)passwordInt);
        }
        return encryptedData;
    }
    private static void deleteFolder(File folder) 
    {
        File[] files = folder.listFiles();
        if (files != null) 
        {
            for (File file : files) 
            {
                if (file.isDirectory()) 
                {
                    deleteFolder(file);
                } 
                else 
                {
                    file.delete();
                }
            }
        }
        folder.delete();
    }
}
