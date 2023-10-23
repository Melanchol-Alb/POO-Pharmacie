import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MedecinConventionne {
	private String nomClinique,nom;
	
	public MedecinConventionne(String nomClinique, String nom) {
		this.nomClinique = nomClinique;
		this.nom = nom;
	}

	public void PlacerCommande(ArrayList<Paire<String,Integer>> meds, JFrame f){
		try {
		File commande = new File("CommandesMedecin.stk");
		FileWriter out = new FileWriter(commande,true);
		// med,nbr-med,nbr....|nom-nomClinique-code
		Date d= new Date();
		String s=d.toString();
		s=s.substring(8,10)+s.substring(4, 7)+s.substring(30)+s.substring(11, 13)+s.substring(14,16)+s.substring(17, 19);
		for(int i=0;i<meds.size();i++)
			out.write(meds.get(i).valeur+","+meds.get(i).valeur2+"-");
		out.write("|"+nom+","+nomClinique+","+s+"\r\n");
		out.close();

		JOptionPane.showMessageDialog(f, "Le code de cette commande est :"+s);
		}
		catch(IOException e) {
			
		}
	}
	
	public void AnnulerCommande(String code,HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter, JFrame f){
		try {
		File commande = new File("CommandesMedecin.stk");
		BufferedReader in = new BufferedReader(new FileReader(commande));
		File interm = new File("CommandesMedecin.int");
		FileWriter out = new FileWriter(interm);
		String str;
		int j;
		boolean notFound=true;
		while(((str=in.readLine())!=null)&&notFound) {
			j=str.length()-1;
			String cod="";
			while(str.charAt(j)!='-') {
				cod=str.charAt(j)+cod;
				j--;
			}
			if(cod.equals(code)){
				j--;
				String nomClin="";
				while(str.charAt(j)!='-') {
					nomClin=str.charAt(j)+nomClin;
					j--;
				}
				j--;
				String name="";
				while(str.charAt(j)!='|') {
					name=str.charAt(j)+name;
					j--;
				}
				if((name.equals(nom))&&(nomClin.equals(nomClinique))){
					j = 0;
					ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
					while(str.charAt(j)!='|') {
						String med="";
						while(str.charAt(j)!=',') {
							med=med+str.charAt(j);
							j++;
						}
						j++;
						String qt = "";
						while(str.charAt(j)!='-') {
							qt+=str.charAt(j);
							j++;
						}
						j++;
						presc.add(new MedicamentPrescrit(med,Integer.parseInt(qt),0));
					}
					Main.PopUpAnnulerCommande(code, true, "", presc);
					notFound=false;
				}
				
				else {
					int r = JOptionPane.showConfirmDialog(f, "Les informations en entrée ne sont pas coherentes avec celles trouvées !\nCode:"+code+"\nMedecin:"+nom+"|"+name+"\nClinique:"+nomClinique+"|"+nomClin+"Ignorer?","Incoherrence!",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(r==JOptionPane.YES_OPTION) {
						j = 0;
						ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
						while(str.charAt(j)!='|') {
							String med="";
							while(str.charAt(j)!=',') {
								med=med+str.charAt(j);
								j++;
							}
							j++;
							String qt = "";
							while(str.charAt(j)!='-') {
								qt+=str.charAt(j);
								j++;
							}
							j++;
							presc.add(new MedicamentPrescrit(med,Integer.parseInt(qt),0));
						}
						Main.PopUpAnnulerCommande(code, true, "", presc);
						notFound=false;
					}
					else out.write(str+"\r\n");
				}
			}
			else out.write(str+"\r\n");
		}
		in.close();
		out.close();
		commande.delete();
		interm.renameTo(commande);
		if (notFound){
			//lot,med,qt-lot,med,qt-......|nom,nomclinique,code
			File servies = new File("CommandesMedecinServies.stk");
			in = new BufferedReader(new FileReader(servies));
			interm = new File("CommandesMedecin.int");
			out = new FileWriter(interm);
			while(((str=in.readLine())!=null)&&notFound) {
				j=str.length()-1;
				String cod="";
				while(str.charAt(j)!=',') {
					cod=str.charAt(j)+cod;
					j--;
				}
				if(cod.equals(code)){
					j--;
					String nomClin="";
					while(str.charAt(j)!=',') {
						nomClin=str.charAt(j)+nomClin;
						j--;
					}
					j--;
					String name="";
					while(str.charAt(j)!='|') {
						name=str.charAt(j)+name;
						j--;
					}
					if((name.equals(nom))&&(nomClin.equals(nomClinique))) {
						j = 0;
						ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
						while(str.charAt(j)!='|') {
							while(str.charAt(j)!=',')
								j++; //Skip le numero de lot
							j++;
							String med="";
							while(str.charAt(j)!=',') {
								med=med+str.charAt(j);
								j++;
							}
							j++;
							String qt = "";
							while(str.charAt(j)!=',') {
								qt+=str.charAt(j);
								j++;
							}
							j++;
							while(str.charAt(j)!='-')
								j++;
							j++;
							presc.add(new MedicamentPrescrit(med,Integer.parseInt(qt),0));
						}
						Main.PopUpAnnulerCommande(code, true, "", presc);	
						notFound=false;
					}
					else {
						int r = JOptionPane.showConfirmDialog(f, "Les informations en entrée ne sont pas coherentes avec celles trouvées !\nCode:"+code+"\nMedecin:"+nom+"|"+name+"\nClinique:"+nomClinique+"|"+nomClin+"Ignorer?","Incoherrence!",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(r==JOptionPane.YES_OPTION) {
							j = 0;
							ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
							while(str.charAt(j)!='|') {
								while(str.charAt(j)!=',')
									j++; //Skip le numero de lot
								j++;
								String med="";
								while(str.charAt(j)!=',') {
									med=med+str.charAt(j);
									j++;
								}
								j++;
								String qt = "";
								while(str.charAt(j)!=',') {
									qt+=str.charAt(j);
									j++;
								}
								j++;
								while(str.charAt(j)!='-')
									j++;
								j++;
								presc.add(new MedicamentPrescrit(med,Integer.parseInt(qt),0));
							}
							Main.PopUpAnnulerCommande(code, true, "", presc);	
							notFound=false;
						}
						else out.write(str+"\r\n");
					}
				}
				else out.write(str+"\r\n");
				if (!notFound) {
					j=0;
					while(str.charAt(j)!='|') {
						String lot ="";
						while(str.charAt(j)!=',') {
							lot +=str.charAt(j);
							j++;
						}
						j++;
						String nomMedicament="";
						while(str.charAt(j)!=',') {
							nomMedicament +=str.charAt(j);
							j++;
						}
						j++;
						String qt="";
						while(str.charAt(j)!=',') {
							qt +=str.charAt(j);
							j++;
						}
						j++;
						String prix="";
						while(str.charAt(j)!=',') {
							prix +=str.charAt(j);
							j++;
						}
						j++;
						String date="";
						while(str.charAt(j)!='-') {
							date +=str.charAt(j);
							j++;
						}
						j++;
						int i = 0;
						System.out.println(nomMedicament+"  "+Stock.get(nomMedicament).size());
						while((i<Stock.get(nomMedicament).size())&&(Stock.get(nomMedicament).get(i).getNumeroLot()!=Integer.parseInt(lot)))
							i++;
						if((Stock.get(nomMedicament).size()==0)||(i==Stock.get(nomMedicament).size())) {
							//----------------------------------------------------------------------------------------------------
							int k = 0;
							while((k<Stock.get(nomMedicament).size())&&(Stock.get(nomMedicament).get(k).getNumeroLot()!=Integer.parseInt(lot)))
								k++;
							if((Stock.get(nomMedicament).size()==0)||(k==Stock.get(nomMedicament).size())) {
								Medicament medd;
								if(inter.get(nomMedicament)!=null)
									medd= inter.get(nomMedicament);
								else medd= exter.get(nomMedicament);
								Stock.get(nomMedicament).add(new MedicamentEnStock(medd, Integer.parseInt(qt), Integer.parseInt(lot), new Date(Long.parseLong(date)), Double.parseDouble(prix)));
							}
							else {
								Stock.get(nomMedicament).get(k).
								setQuantité(Stock.get(nomMedicament).get(k).getQuantité()+Integer.parseInt(qt));
							}
							//----------------------------------------------------------------------------------------------------------
						}
						else {
							Stock.get(nomMedicament).get(i).setQuantité(Stock.get(nomMedicament).get(i).getQuantité()+Integer.parseInt(qt));
						}
					}
				}
			}
			Donnees.EnregistrerStock(Stock,exter,inter);
			in.close();
			out.close();
			servies.delete();
			interm.renameTo(servies);
		}
		}
		catch(IOException e) {
			
		}
	}

	public static ArrayList<Paire<JFrame, Paire<Integer, Integer>>> ServirCommandes(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter){
		ArrayList<Paire<JFrame, Paire<Integer, Integer>>> list = new ArrayList<Paire<JFrame, Paire<Integer, Integer>>>();
		try {
		File Commandes = new File("CommandesMedecin.stk");
		BufferedReader in = new BufferedReader(new FileReader(Commandes));
		File Servies = new File("CommandesMedecinServies.stk");
		FileWriter out = new FileWriter(Servies,true);
		File interm = new File("CommandesMedecin.int");
		FileWriter outint = new FileWriter(interm);
		String str;
		while((str=in.readLine())!=null) {
			int j =0;
			ArrayList<Paire<String,Integer>> med = new ArrayList<Paire<String,Integer>>();
			boolean fullListAvailable = true;
			while((str.charAt(j)!='|')&&(fullListAvailable)) {
				String nom ="";
				while(str.charAt(j)!=',') {
					nom+=str.charAt(j);
					j++;
				}
				j++;
				String qt="";
				while(str.charAt(j)!='-') {
					qt+=str.charAt(j);
					j++;
				}
				j++;
				if(Stock.get(nom)!=null) {
					int i =0;
					while((i<Stock.get(nom).size())&&(Stock.get(nom).get(i).getQuantité()<Integer.parseInt(qt))) 
						i++;
					if((i<Stock.get(nom).size())&&(Stock.get(nom).get(i).getQuantité()>=Integer.parseInt(qt))) {
						med.add(new Paire<String,Integer>(nom,Integer.parseInt(qt)));
					}//else y a pas de lots valables
					else {
						fullListAvailable = false;
						outint.write(str+"\r\n");
					}
				}//else le medicament n'existe pas dans le stock
				else {
					fullListAvailable = false;
					outint.write(str+"\r\n");
				}
			}
			if(fullListAvailable) {
				for (int k=0;k<med.size();k++) {
					int i =0;
					while((i<Stock.get(med.get(k).valeur).size())&&(Stock.get(med.get(k).valeur).get(i).getQuantité()<med.get(k).valeur2)) 
						i++;
					if(Stock.get(med.get(k).valeur).get(i).getQuantité()>=med.get(k).valeur2) {
						Stock.get(med.get(k).valeur).get(i).setQuantité(Stock.get(med.get(k).valeur).get(i).getQuantité()-med.get(k).valeur2);
						out.write(Stock.get(med.get(k).valeur).get(i).getNumeroLot()+","+med.get(k).valeur+","+med.get(k).valeur2+","+Stock.get(med.get(k).valeur).get(i).getPrix()+","+Stock.get(med.get(k).valeur).get(i).getDateExpiration().getTime()+"-");//lot,nom,qt,prix,dateexp-
					}
				}
				String nm="",clin="",cod="";
				j=str.length()-1;
				while(str.charAt(j)!=',') {
					cod=str.charAt(j)+cod;
					j--;
				}
				j--;
				while(str.charAt(j)!=',') {
					clin=str.charAt(j)+clin;
					j--;
				}
				j--;
				while(str.charAt(j)!='|') {
					nm=str.charAt(j)+nm;
					j--;
				}
				out.write("|"+nm+","+clin+","+cod+"\r\n");
				Paire<Integer, Integer> p = new Paire<Integer, Integer>(0,0);
				list.add(new Paire<JFrame, Paire<Integer, Integer>>(Main.ServirCommandeClinique(clin, cod, p), p));
			}
		}
		in.close();
		out.close();
		outint.close();
		Commandes.delete();
		interm.renameTo(Commandes);
		Donnees.NettoyerStock(Stock, exter, inter);
		}
		catch(IOException e) {
		}
		return list;
	}

	public void RetireCommande(String code, JFrame f) {
		try {
		File Servies = new File("CommandesMedecinServies.stk");
		BufferedReader in = new BufferedReader(new FileReader(Servies));
		File interm = new File("CommandesMedecin.int");
		ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
		FileWriter outint = new FileWriter(interm);
		String str,cod;
		int j;
		boolean notFound = true;
		while (((str=in.readLine())!=null)&&(notFound)) {
			j=str.length()-1;
			cod="";
			while(str.charAt(j)!=',') {
				cod=str.charAt(j)+cod;
				j--;
			}
			if(!cod.equals(code))
				outint.write(str+"\r\n");
			else {
				j=0;
				double total = 0;
				notFound = false;
				while(str.charAt(j)!='|') {
					String lot="";
					while(str.charAt(j)!=',') {//lot
						lot+=str.charAt(j);
						j++;
					}
					j++;
					String medic ="";
					while(str.charAt(j)!=',') {//nom
						medic+=str.charAt(j);
						j++;
					}
					j++;
					String quantité="";
					while(str.charAt(j)!=',') {
						quantité+=str.charAt(j);
						j++;
					}
					j++;
					String prix ="";
					while(str.charAt(j)!=',') {
						prix+=str.charAt(j);
						j++;
					}
					j++;
					while(str.charAt(j)!='-') {//date peremption
						j++;
					}
					j++;
					presc.add(new MedicamentPrescrit(medic, Integer.parseInt(quantité), Integer.parseInt(lot)));
					total+=Double.parseDouble(prix)*Integer.parseInt(quantité);
				}
				Main.TransactionSucces(total, presc);
			}
		}
		if(notFound)
			JOptionPane.showMessageDialog(f, "La commande"+code+" n'a pas pu être retirée!");
		in.close();
		outint.close();
		Servies.delete();
		interm.renameTo(Servies);
	}
	catch(IOException e) {
		
	}
	}
}