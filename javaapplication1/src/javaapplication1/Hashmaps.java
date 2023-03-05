package javaapplication1;

import java.util.HashMap;

public class Hashmaps {
	
	public static void main(String[] args)
{
	
	HashMap<String, String> Password = new HashMap<String, String>();
	Password.put("admin", "password");
	Password.put("lortiz", "husky");
	Password.put("kenya", "cats");
	Password.put("daniel", "basketball");
	Password.put("brian", "gaming");
	Password.put("jacky", "food");
	
	System.out.println(Password.values());

	}


}
