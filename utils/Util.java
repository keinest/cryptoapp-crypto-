package crypto.utils;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Util
{
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public static boolean veri_number_format(String number)
    {
        int A = 0;
        for(int i = 0; i < number.length(); i++)
        {
            if(number.charAt(0) == '.')
                A += 1;
            else if(number.charAt(i) == '.')
                A += 1;
            if(A > 1)
                return false;
                
            else if(number.charAt(i) < '0' || number.charAt(i) > '9')
                return false;
        }

        return true;
    }

    public static boolean veri_user_enter(String word)
    {
        return word.matches("\\d+");
    }

    public static int convert_user_enter(String word)
    {
        try{return Integer.parseInt(word);} 
       
        catch(NumberFormatException e){return -1;}
    }

    public static int pgcd(int a,int b)
    {
        int temp = 0;
        while(b != 0)
        {
            temp = b;
            b    = a % b;
            a    = temp;
        }

        return a;
    }

    public static long extendEuclide(long a, long b, long[] xy) 
    {
        if(a == 0) 
        {
            xy[0] = 0;
            xy[1] = 1;
            return b;
        }

        long[] xy1 = new long[2];
        long pgcd  = extendEuclide(b % a, a, xy1);
        xy[0]      = xy1[1] - (b / a) * xy1[0];
        xy[1]      = xy1[0];
        return pgcd;
    }

    public static boolean isPrime(int a,int b)
    {
        if(pgcd(a,b) != 1) return false;
        return true;
    }

    public static int power(int a,int e)
    {
        if (e < 0)
            throw new IllegalArgumentException("Exponent must be non-negative.");
        
        if(e == 0)
            return 1;

        long base = a;
        int exponent = e;
        long result = 1;

        while(exponent > 0) 
        {
            if((exponent & 1) == 1) 
                result = result * base;

            base = base * base;
            exponent >>= 1;
        }

        return (int) result;
    }
    
    public static boolean mailVeri(String email)
    {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static List<Integer> convert_to_bin(String str)
    {
        List<Integer> binary_value = new ArrayList<>();

        for(int i = 0; i < str.length(); i++) 
        {
            int value = str.charAt(i);
            for(int j = 7; j >= 0; j--) 
                binary_value.add((value >> j) & 1); 
        }

        return binary_value;
    }

    public static String convert_to_string(List<Integer> binary_value)
    {
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < binary_value.size(); i += 8) 
        {
            int charValue = 0;
            for(int j = 0; j < 8; j++) 
                charValue = (charValue << 1) | binary_value.get(i + j);

            str.append((char) charValue);
        }

        return str.toString();
    }

    public static void copyText(String text)
    {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }
    
    public static boolean isStrongPassword(String password) 
    {
        if(password.length() < 8) return false;
        
        boolean hasUpper   = false;
        boolean hasLower   = false;
        boolean hasDigit   = false;
        boolean hasSpecial = false;
        
        for(char c : password.toCharArray()) 
        {
            if(Character.isUpperCase(c)) hasUpper = true;
            else if(Character.isLowerCase(c)) hasLower = true;
            else if(Character.isDigit(c)) hasDigit = true;
            else if(!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    public static String formatDuration(long milliseconds) 
    {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }
}
