package crypto.utils;

import java.util.List;
import java.util.ArrayList;

public class Util
{
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
        if(e == 0) return 1;

        return power(a,e - 1);
    }
    
    public static boolean mailVeri(String email)
    {
        if(email.length() < 10)
            return false;
        
        for(int i = 0; i < email.length() - 10; i++)
        {
            if(email.charAt(i) == '@' || (email.charAt(i) >= 'A' && email.charAt(i) <= 'Z'))
                return false;
        }

        if(email.charAt(email.length() - 10) == '@' && email.charAt(email.length() - 9) == 'g' && email.charAt(email.length() - 8)  == 'm' 
            && email.charAt(email.length() - 7)  == 'a' && email.charAt(email.length() - 6)== 'i' && email.charAt(email.length() - 5) == 'l' 
            && email.charAt(email.length() - 4) == '.' && email.charAt(email.length() - 3) == 'c' && email.charAt(email.length() - 2) == 'o' 
            && email.charAt(email.length() - 1) == 'm'
        )
            return true;

        return false;
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
}
