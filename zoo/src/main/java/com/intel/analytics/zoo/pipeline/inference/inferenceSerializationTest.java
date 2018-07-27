package com.intel.analytics.zoo.pipeline.inference;
import java.io.*;

public class inferenceSerializationTest{
    public static void main(String args[]) throws Exception{

		File file = new File("model.out");

		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		TestClassificationModel model = new TestClassificationModel();
		model.load("/home/xiaxue/data/text-classification/textClassificationModel");
		out.writeObject(model);
		out.close();

		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fis);
		TestClassificationModel newmodel = (TestClassificationModel) in.readObject();
		in.close();
		System.out.println(newmodel.toString());

	}
}
