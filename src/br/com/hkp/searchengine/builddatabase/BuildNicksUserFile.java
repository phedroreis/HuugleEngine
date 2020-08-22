package br.com.hkp.searchengine.builddatabase;


import br.com.hkp.searchengine.json.JsonReader;
import br.com.hkp.searchengine.json.UserJson;
import static br.com.hkp.searchengine.json.UserJson.USER_ID_INDEX;
import static br.com.hkp.searchengine.json.UserJson.USER_NICK_INDEX;
import br.com.hkp.searchengine.registers.NicksUserReg;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.nicksUserFilename;
import br.com.hkp.searchengine.util.Util;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

/*******************************************************************************
 * Constroi um arquivo de indice contendo registros de todos os nicks e user IDs
 * catalogados do CC. 
 * 
 * @author "Pedro Reis"
 * @since 6 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class BuildNicksUserFile
{
    private static BuildLog buildLog;//constroi arquivo de log
       
   /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void make(final TreeSet<NicksUserReg> index) 
        throws IOException
    {
        NicksUserReg nicksUserReg = 
            new NicksUserReg(datDirName + nicksUserFilename);
            
        nicksUserReg.openToWrite();
        
        buildLog.writeln("Gravando arquivo dat...\n");
        
        int count = 0;
        for (NicksUserReg reg: index)
        {
            count++;
            nicksUserReg.setNick(reg.getNick());
            nicksUserReg.setUserID(reg.getUserID());
            nicksUserReg.write();
            buildLog.writeln(nicksUserReg.toString());
        }//for
        
        nicksUserReg.close();
        
        buildLog.writeln
        (
            "\n" + count + " registros gravados em " + 
            datDirName + nicksUserFilename
        );
            
    }//make() 
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Constroi arquivo .dat que associa nick (PK) a sua respectiva userID
     * 
     * @param args n/a
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            buildLog = new BuildLog("/nicks_user.log", 1024000);
            
            Global.initializeFilenames();
            
            TreeSet<NicksUserReg> index = new TreeSet<>(new NicksComparator());
    
            UserJson userJson = new UserJson();

            JsonReader<UserJson> jsonReader = 
                new JsonReader<>(userJson, "/users.json", 512000);
            
            buildLog.writeln("Lendo arquivo users.json...\n");
               
        
            jsonReader.open(); 
            int canonicos = 0; int duplicate = 0;
            while ((userJson = jsonReader.read()) != null)
            {
                String nick = userJson.getValue(USER_NICK_INDEX);
                nick = nick.replace("&quot;", "\"").
                            replace("&#039;", "'").
                            replace("&gt;", ">").
                            replace("&lt;", "<");
                
                String userID = userJson.getValue(USER_ID_INDEX);
                NicksUserReg nicksUserReg = new NicksUserReg(null);
                nicksUserReg.setNick(nick);
                nicksUserReg.setUserID(userID);
                
                if (!index.add(nicksUserReg))
                {
                    buildLog.writeln
                    (
                        "Nick duplicado -> " + nicksUserReg.toString()
                    );
                    duplicate++;
                }//if
                else
                    canonicos++;
                               
                buildLog.writeln(nicksUserReg.toString());
               
                if (!nick.matches("[\\sa-z0-9]+"))
                {
                    nicksUserReg = new NicksUserReg(null);
                    nicksUserReg.setNick(Util.stripAccents(nick));
                    nicksUserReg.setUserID(userID);
                    if (!index.add(nicksUserReg))
                    {
                        buildLog.writeln("DIACRITIZADO DUPLICADO");
                        if (index.remove(nicksUserReg))
                            buildLog.writeln(" <- Antigo removido");
                        if (index.add(nicksUserReg))
                            buildLog.writeln(" -> Novo adicionado");
                    }
                    buildLog.writeln(nicksUserReg.toString()); 
                }//if
                
                System.out.println(jsonReader.getCountRead());
        
            }//while
            jsonReader.close();
                       
            System.out.println("\n" + index.size() + " nicks registrados\n");
            
            buildLog.writeln("\n" + index.size() + " nicks registrados");
            buildLog.writeln(canonicos + " canonicos");
            buildLog.writeln(duplicate + " duplicados");
            buildLog.writeln((canonicos + duplicate) + " registros lidos\n");
            
            make(index);
            
            buildLog.close();  
              
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }//main()
    
    /***************************************************************************
    *
    ***************************************************************************/
    private static class NicksComparator implements Comparator<NicksUserReg>
    {
        @Override
        public int compare(NicksUserReg r1, NicksUserReg r2)
        {
            return (r1.getKey()).compareTo(r2.getKey());
        }        
    }//classe NicksComparator
   
}//classe BuildNicksUserFile

