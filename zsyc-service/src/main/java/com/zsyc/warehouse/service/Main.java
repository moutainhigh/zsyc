package com.zsyc.warehouse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		t();
	}

	public static void t(){
		Map<String,List<Model>> map = new HashMap<String,List<Model>>();
		List<Model> l1 = new ArrayList<Model>();
		l1.add(new Model("ţ��",3,"kg"));
		l1.add(new Model("ţ��",2,"kg"));
		l1.add(new Model("ţ��",5,"kg"));

		List<Model> l2 = new ArrayList<Model>();
		l2.add(new Model("����",5,"��"));
		l2.add(new Model("����",1,"��"));
		l2.add(new Model("����",1,"kg"));
		l2.add(new Model("����",7,"��"));
		l2.add(new Model("����",3,"kg"));


		map.put("sku1", l1);
		map.put("sku2", l2);
		Iterator<Entry<String, List<Model>>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, List<Model>> entry =iterator.next();
			List<Model> model = entry.getValue();

			/*int sum = 0;
			for(Model m : model){
				sum+=m.getNum();
			}
			System.out.println("sku:"+entry.getKey()+",sum:"+sum);*/

			System.out.println("sku:"+entry.getKey()+",message:"+splitModel(model));
		}
	}

	public static String splitModel(List<Model> modelList){
		Set<String> unitSet = new HashSet<String>();
		for(Model m : modelList){
			unitSet.add(m.getUnit());
		}
		String message = "";
		for(String unit :unitSet ){
			int sum = 0;
			for(Model model :modelList){
				if(unit.equals(model.getUnit())){
					sum+=model.getNum();
				}
			}
			message+=sum+unit+",";
		}
		return modelList.get(0).getName()+":"+message.substring(0,message.lastIndexOf(","));
	}
}
