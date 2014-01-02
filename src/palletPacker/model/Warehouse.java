package palletPacker.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Warehouse {
	
	public Package[] packages;
	public Pallet[] pallets;
	String dataFilePath;
	
	public Warehouse(String dataFilePath)
	{
		this.dataFilePath = dataFilePath;
		try {
			ReadData();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Pallet getPalletByName(String name)
	{
		for(int i=0;i<pallets.length;i++)
		{
			if(pallets[i].id.equals(name))
			{
				return pallets[i];
			}
		}		
		return null;
	}
	
	private void ReadData() throws NumberFormatException, IOException
	{
		File dataFile = new File(dataFilePath);
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		int palletsAmount = Integer.parseInt(br.readLine());
		
		pallets = new Pallet[palletsAmount];
		
		for(int i = 0;i<palletsAmount;i++)
		{
			String line = br.readLine();
			String[] lineSplitted = line.split("\t");			
			int v = Integer.parseInt(lineSplitted[1]);
			float h= Float.parseFloat(lineSplitted[2]);
			float hMax =Float.parseFloat(lineSplitted[3]); 
			Pallet p = new Pallet(lineSplitted[0], v, h, hMax);
			pallets[i] = p;
		}
		
		int packagesAmount = Integer.parseInt(br.readLine());
		packages = new Package[packagesAmount];
		for(int i=0;i<packagesAmount;i++)
		{
			String line = br.readLine();
			String[] lineSplitted = line.split("\t");
			int v = Integer.parseInt(lineSplitted[1]);
			String defaultPalletName = lineSplitted[2];
			Pallet defPallet = getPalletByName(defaultPalletName);
			Pallet[] compPallets = null;
			if(lineSplitted.length>3)
			{
				compPallets = new Pallet[lineSplitted.length-3];
				
				for(int j =0;j<lineSplitted.length-3;j++)
				{
					compPallets[j] = getPalletByName(lineSplitted[j+3]);
				}
				
			}			
			Package p = new Package(lineSplitted[0], v, defPallet, compPallets);
			packages[i] = p;
		}
		br.close();		
	}

}
