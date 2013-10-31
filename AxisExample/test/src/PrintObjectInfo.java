import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;

public class PrintObjectInfo {
	public static void PrintObjectField(Object obj) {
		try {
			Class cls = obj.getClass();
			if (cls != null) {
				Field[] f = cls.getDeclaredFields();
				AccessibleObject[] accObj = f;
				Field.setAccessible(accObj, true);
				for (Field field : f) {
					System.out.print(field.getName() + ":" + field.get(obj)
							+ "\t");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PrintListObject(List list) {
		for (Object obj : list) {
			PrintObjectField(obj);
			System.out.println();
		}
	}
	
	public static void PrintListObject(Object[] list) {
		for (Object obj : list) {
			PrintObjectField(obj);
			System.out.println();
		}
	}
}
