package mainpackage;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import gui_main.Main_Frame;
import gui_setup.MainSetupFrame;
import lang_res.Consts;

class Main_Test {
	public static void main(String args[]) {
		
		URL keyFileURL = Main_Test.class.getClassLoader().getResource("data");
		//TODO: NOT DELETING for purpose testing
		//Locale currentLocale = Locale.US;
		Locale currentLocale = Locale.getDefault();
		System.out.println(currentLocale);
		ResourceBundle bundle = ResourceBundle.getBundle(Consts.FILENAME, currentLocale);
		if (keyFileURL == null) {
			new MainSetupFrame(bundle);
		}else {
			new Main_Frame(bundle);		
		}
			
		
		
		//For test purposes TODO: NOT DELETE
		
		/*String[] properties = {ALL_CONST.USER, ALL_CONST.PASSWORD, ALL_CONST.PORT, ALL_CONST.DB};
		String[] values = {"root", "", "3306", "mydb"};
		Methods.editProperties(Main_Test.class, properties, values);*/
		//Methods.createPropertyFile(Main_Test.class, "1", "1", "1", "1");
		//new Main_Frame();
		//Methods.connectionTest();
		/*try {
			Methods.getPrimaryKeyColumnsForTable(Methods.connectionToDb("mydb"), "test_table");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*Object[] o = {1};
		try {
			Methods.getPrimaryKeyColumnsData("mydb", "test_table",Methods.getPrimaryKeyColumnsForTable(Methods.connectionToDb("mydb"), "test_table"), o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		
	}	
}