package test.length;
/**
 * 作者：齐潮
 * 创建日期：2016年12月21日
 * 类说明：
 */
public class TestStatic {

	private static int a;
	
	public void init(int b){
		TestStatic.a = b;
	}
	
	public int getA(){
		return TestStatic.a;
	}
	
}
