package ideal.tests;

import typestate.test.helper.File;
import typestate.test.helper.ObjectWithField;

/**
 * Hello world!
 *
 */
public class FileMustBeClosedTest
{

	public static void main( String[] args )
    {
        FileMustBeClosedTest test = new FileMustBeClosedTest();
//        test.noSeedTest();
//        test.newSeedTest();
//        test.simple0();
//        test.simple1();
//        test.branching();
//        test.aliasing();
//        test.summaryTest();
//        test.interprocedural();
//        test.noStrongUpdate();
//        test.noStrongUpdatePossible();
        test.test();
    }
	
	public void newSeedTest() {
		File file = new File();
		File temp = new File();
		file.open();
		temp.open();
	}
	
	public void noSeedTest() {
		File file = new File();
		File temp = file;
		file.open();
		temp.close();
	}

	/**
	 * This method can be used in test cases to create branching. It is not
	 * optimized away.
	 * 
	 * @return
	 */
	protected boolean staticallyUnknown() {
		return true;
	}

	public void simple0() {
		File file = new File();
		file.open();
		file.close();
	}

	public void simple1() {
		File file = new File();
		File alias = file;
		alias.open();
		alias.close();
	}

	public void branching() {
		File file = new File();
		if (staticallyUnknown())
			file.open();
	}

	public void aliasing() {
		File file = new File();
		File alias = file;
		if (staticallyUnknown())
			file.open();
	}

	public void summaryTest() {
		File file1 = new File();
		call(file1);
		file1.close();
		File file = new File();
		File alias = new File();
		call(alias);
		file.close();
	}

	private static void call(File alias) {
		alias.open();
	}

	public void interprocedural() {
		File file = new File();
		file.open();
		flows(file, true);
		file.close();
	}

	private static void flows(File file, boolean b) {
		if (b)
			file.close();
	}

	public void flowViaField() {
		ObjectWithField container = new ObjectWithField();
		flows(container);
		if (staticallyUnknown())
			container.field.close();
	}

	private static void flows(ObjectWithField container) {
		container.field = new File();
		File field = container.field;
		field.open();
	}

	public void indirectFlow() {
		ObjectWithField a = new ObjectWithField();
		ObjectWithField b = a;
		flows(a, b);
	}

	private void flows(ObjectWithField a, ObjectWithField b) {
		File file = new File();
		file.open();
		a.field = file;
		//		File alias = b.field;
		//		alias.close();
	}

	public void noStrongUpdatePossible() {
		File b = null;
		File a = new File();
		a.open();
		File e = new File();
		e.open();
		if (staticallyUnknown()) {
			b = a;
		} else {
			b = e;
		}
		b.close();
	}

	public void parameterAlias() {
		File file = new File();
		File alias = new File();
		call(alias, file);
	}

	private void call(File file1, File file2) {
		file1.open();
		file2.close();
	}

	public void parameterAlias2() {
		File file = new File();
		File alias = file;
		call2(alias, file);
		alias.open();
	}

	private void call2(File file1, File file2) {
		file1.open();
		if (staticallyUnknown())
			file2.close();
	}




	//	
	public void test() {
		ObjectWithField a = new ObjectWithField();
		ObjectWithField b = a;
		File file = new File();
		file.open();
		bar(a, b, file);
//		b.field.close();
	}

	public void noStrongUpdate() {
		ObjectWithField a = new ObjectWithField();
		ObjectWithField b = new ObjectWithField();
		File file = new File();
		if (staticallyUnknown()) {
			b.field = file;
		} else {
			a.field = file;
		}
//		a.field.open();
//		b.field.close();
		// Debatable
	}

	public void unbalancedReturn1() {
		File second = createOpenedFile();
		System.out.println(second);
	}

	public void unbalancedReturn2() {
		File first = createOpenedFile();
		clse(first);
		File second = createOpenedFile();
		second.hashCode();
	}

	private static void clse(File first) {
		first.close();
	}

	public static File createOpenedFile() {
		File f = new File();
		f.open();
		return f;
	}

	private void bar(ObjectWithField a, ObjectWithField b, File file) {
		a.field = file;
	}

	public void lateWriteToField() {
		ObjectWithField a = new ObjectWithField();
		ObjectWithField b = a;
		File file = new File();
		bar(a, file);
		File c = b.field;
		c.close();
	}

	private void bar(ObjectWithField a, File file) {
		file.open();
		a.field = file;
	}

	public void fieldStoreAndLoad1() {
		ObjectWithField container = new ObjectWithField();
		File file = new File();
		container.field = file;
		File a = container.field;
		a.open();
	}

	public void fieldStoreAndLoad2() {
		ObjectWithField container = new ObjectWithField();
		container.field = new File();
		ObjectWithField otherContainer = new ObjectWithField();
		File a = container.field;
		otherContainer.field = a;
		flowsToField(container);
	}

	private void flowsToField(ObjectWithField container) {
		File field = container.field;
		field.open();
	}

	public void wrappedClose() {
		File file = new File();
		File alias = file;
		alias.open();
		file.wrappedClose();
	}

	public void multipleStates() {
		File file = new File();
		file.open();
		int x = 1;
		System.out.println(x);
		file.close();
		x = 1;
		System.out.println(x);
	}

	public void doubleBranching() {
		File file = new File();
		if (staticallyUnknown()) {
			file.open();
			if (staticallyUnknown())
				file.close();
		} else if (staticallyUnknown())
			file.close();
		else {
			System.out.println(2);
		}
	}

	public void whileLoopBranching() {
		File file = new File();
		while(staticallyUnknown()){
			if (staticallyUnknown()) {
				file.open();
				if (staticallyUnknown())
					file.close();
			} else if (staticallyUnknown())
				file.close();
			else {
				System.out.println(2);
			}
		}
	}
}
