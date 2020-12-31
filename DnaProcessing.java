/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dna.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author PC
 */
public class DnaProcessing {
    //create dna file
     private static File file = new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\Dna Processing\\src\\dna\\processing\\GRch38dnapart.fa");
     
     //method that finds the stop codon in a dna strand
    public static int findStopCodon(String dna,int startIndex, String stopCodon){
      //initialize stop index variable to the index of the stopcodon in the dna string starting from the start index
      int stopIndex = dna.indexOf(stopCodon,startIndex);
      //repeat steps
      while(true){
        //check if the stop index is a multiple of 3
        if((stopIndex-startIndex) % 3 == 0 ) {
        return stopIndex;
        }
        //return -1 if it doesn't find the stop codon
        else if(stopIndex ==-1)return -1;
        //start looking for the stop codon after the first index
        stopIndex = dna.indexOf(stopCodon, stopIndex+1);
      } 
    }
    //method that finds the closests stop codon in a gene
    public static int minCodon(String dna, int startIndex,int taa, int tag, int tga){
            //initialize a minimum index variable
            int minIndex;
            //find the minimum index of all the stop codons
            //compare tag and taa, check if tag < taa and set minindex to tag,
            if(taa ==-1 || (tag !=-1 && tag<taa)){ 
                minIndex = tag;
            }
             //else set minindex to taa
            else{minIndex = taa;}
            //compare (new minindex) and tga , if tga is less set minindex to tga
            if(minIndex ==-1 || (tga!= -1 && tga<minIndex)) {
              minIndex= tga;
            }
            return minIndex;

    }
    public static String findGene(String dna, int index){
    ///converts the dna string to uppercase if it's lowercase    
    if(dna.equals(dna.toLowerCase())) dna = dna.toUpperCase();

    //initialize the start index to the first occurence of ATG in the dna string
    int startIndex = dna.indexOf("ATG", index);
    //check if there is no ATG and return the empty string
    if(startIndex ==-1) return "";
    //initialize the indices of all the stop codons
    int taa = findStopCodon(dna,startIndex,"TAA");
    int tag = findStopCodon(dna,startIndex,"TAG");
    int tga = findStopCodon(dna,startIndex,"TGA");
    //find the minimum codon
    int minIndex = minCodon(dna,startIndex,taa,tag,tga);

    while(true) {
        //if no codon is found for the first occurence of atg
        if(minIndex ==-1){
            //start looking for atg after the first occurence
            startIndex = dna.indexOf("ATG",startIndex+1);
            //find the minimum codon again
            minIndex = minCodon(dna,startIndex,taa,tag,tga);
        } 
        break;
    }
    //if the minindex is -1  no valid stop codon is found
    if(minIndex == -1) {return "";}
    //return the gene which is a substring of the dna starting from the start index and stopping at the minimum index
    return dna.substring(startIndex,minIndex+3);
    }

    public static ArrayList<String> getAllGenes(String dna){
        //create arraylist to hold all genes
        ArrayList<String> geneList = new ArrayList<>();
        //initialize currGene variable that holds the first gene found in the dna string
        String currGene;
        //initialize currIndex integer that holds the index of the first gene in the dna string
        int currIndex= 0;
        //repititive loop
        while(true){
           //update currGene to find the gene after the first index
           currGene = findGene(dna, currIndex);
           //check if there is no more gene in the dna string
           if(currGene.isEmpty()){
            //break out of the loop
            break;
            }
           //store the gene in the array list
           geneList.add(currGene);
           //set currIndex to the index after the first gene
           currIndex = dna.indexOf(currGene,currIndex)+ currGene.length();
        }
        return geneList;
        
    }

    public static float cgRatio(String dna){
    //initialize ratio variable
    float ratio;
    //initialize cCount,gCount,allCount,CIndex, and gIndex variables
    int cCount=0,gCount=0,allCount,cIndex = dna.indexOf("C"), gIndex = dna.indexOf("G");
    //check if "C" and "G" exist in the dna string
    while(true){
        //increment c and g count
        if(cIndex!=-1) cCount++;
        if(gIndex!=-1)gCount++;
        if(cIndex ==-1 &&gIndex==-1)break;
        //update the values of C and G index to find them after the first occurence
       
        if(cIndex!=-1)cIndex = dna.indexOf("C",cIndex+1);
        if(gIndex!=-1)gIndex = dna.indexOf("G",gIndex+1);
    }
    //update allcount to the sum of c and g count
    allCount = cCount+ gCount;
    //update ratio to the value of allcount divided by the length of the dna strand
    ratio = (float) allCount/dna.length();
    //return the ratio
    return ratio;
    }
    
    public static int countCTG(String dna){
        //initalize count and ctg index variables
    int count = 0,ctgIndex = dna.indexOf("CTG");
    //check if ctg exists in the dna strand
    while(ctgIndex!=-1){
        //increment count
        count++;
        //update the value of ctg index to after the first occurence
        ctgIndex = dna.indexOf("CTG",ctgIndex+1);
    }
    //return the count
    return count;
    }
    public static void processGenes(ArrayList<String> dna){
        int overSixtyCount=0,cgCount = 0,lgLength = 0;
        for(String gene: dna){
           int geneLength=gene.length();
            if(geneLength > 60){
                overSixtyCount ++;
                //System.out.println("Gene longer than 60 characters is: \n"+gene);
            }
            if(cgRatio(gene) >0.35){
                cgCount++;
                //System.out.println("gene with a CG ratio greater than 0.35 is : "+gene);
            }
            if(geneLength > lgLength) lgLength = geneLength;
        }
           System.out.println("The number of genes longer than 60 characters is: " +overSixtyCount);
           System.out.println("The number of genes with cgRatio greater than .35 is: "+cgCount);
           System.out.println("The length of the longest gene is: "+lgLength);
    
    
    }   

    public static void main(String[] args) throws IOException {
       //read the file holding the dna into a single string
        String dna = FileUtils.readFileToString(file);
        //create array list to hold all the genes in the dna
        ArrayList<String> forDna = getAllGenes(dna);
     
        System.out.println("FOR DNA STRING FROM FILE \n######################");
         //print ctg count
        System.out.println("The ctg count of the dna strand is: "+countCTG(dna));
        //run process gene on the dna strand
        processGenes(forDna);
    }
    
}
