/*
 * @author: Mohnish Thallavajhula
 * @WKU ID: 800606747
 */
import java.io.*;
import java.util.ArrayList;

public class Disk {

   char[] filename;
   int nblocks;
   RandomAccessFile raFile;
   ArrayList<BufferList> cacheBuffer = new ArrayList<BufferList>();

   public Disk(char[] filename, int nblocks) throws IOException {
      this.filename = filename;
      this.nblocks = nblocks;

      File fileName = new File(new String(FileSystem.fileName));
      if (!fileName.exists()) {
         raFile = new RandomAccessFile(fileName, "rw");
         raFile.setLength(this.nblocks * FileSystem.blockSize);
      } else {
         raFile = new RandomAccessFile(fileName, "rw");
      }
   }

   public int writeBlock(int blockNum, byte[] buffer) throws IOException {
      //writeBlock method
      //writeSuccess is an integer variable set to 0 if writing is successful else 1
      int writeSuccess = 0;
      try {
         //dirtyBit is set to true by default
         //this will be set to false, if there is redundant data either in the cache
         //or on the disk
         boolean dirtyBit = true;
         if (cacheBuffer.size() < 3) {//3 indicates the size of the cache
            //adding the blockNum, corresponding buffer and it's corresponding dirtyBit to cache
            cacheBuffer.add(new BufferList(blockNum, buffer, dirtyBit));
            //checking if thre is redundancy in the cache of the disk
            if (checkRedundancy()) {
               //setting the dirtyBit value of the latest element to false indicating that the
               //corresponding 'buffer' value is already existing either in the cache
               //or on the disk
               cacheBuffer.get(cacheBuffer.size() - 1).dirtyBit = false;
            }
            //this method prints the cache contents
            printData();
         } else {//if data being inserted into the queue is at the 4th position of queue
            //seeking to the blocknumber on the disk
            raFile.seek(cacheBuffer.get(0).blockNum * FileSystem.blockSize);
            //checking if the dirtyBit is true/false
            //if false, data is not written to disk, else it is written since
            //it is fresh data
            if (cacheBuffer.get(0).dirtyBit) {
               raFile.write(cacheBuffer.get(0).buffer, 0, FileSystem.blockSize);
            }
            writeSuccess = 0;
            //this is the queue implementation - FIFO
            //we remove the 1st element i.e. 0th element and add data at the ending of the queue
            cacheBuffer.remove(0);
            //now we are adding the fresh data to the 3rd position
            //Summary: if the data being written is waiting at the 4th position
            //then we copy the contents of the 1st position to disk
            //remove the contents from the 1st position and then shift the positions  of the
            //existing elements, moving them forward and then making the last position vacant
            // and then adding the data waiting at the 4th position to the 3rd position :)
            cacheBuffer.add(new BufferList(blockNum, buffer, dirtyBit));
            if (checkRedundancy()) {
               cacheBuffer.get(cacheBuffer.size() - 1).dirtyBit = false;
            }
            //printing the contents of the cache
            printData();
         }
      } catch (EOFException ex) {
         System.out.println("Reached end of file. Cannot write further." + ex);
         writeSuccess = 1;
      }
      return writeSuccess;
   }

   public int readBlock(int blockNum, byte[] buffer) throws IOException {
      int readSuccess = 0;
      try {//checking if there is latest and fresh data present in the cache
         for (int loop = cacheBuffer.size()-1; loop >0 ; loop--) {
            if (blockNum == cacheBuffer.get(loop).blockNum) {
               System.out.println(new String(cacheBuffer.get(loop).buffer).trim());
               readSuccess = 0;
               return readSuccess;
            }
         }
         //if no fresh data is present in cache, then we seek and read data from disk
         raFile.seek(blockNum * FileSystem.blockSize);
         raFile.read(buffer);
         //displaying contents of disk on console
         System.out.println("Block Content on disk:\n" + new String(buffer).trim());
         readSuccess = 0;
      } catch (IOException e) {
         System.out.println("Error reading the contents of file:" + e);
         readSuccess = 1;
      }
      return readSuccess;
   }

   public void syncDisk() throws IOException {
      //writing all the values present in the cache to the disk
      //here before writing to the disk we check if the data is redundant by checking the
      //value of the dirtyBit. if redundant data is present, that specific data is not
      //written to the disk
      for (int loop = 0; loop < cacheBuffer.size(); loop++) {
         raFile.seek(cacheBuffer.get(loop).blockNum * FileSystem.blockSize);
         if (cacheBuffer.get(loop).dirtyBit) {
            raFile.write(cacheBuffer.get(loop).buffer);
         }
      }
      //clearing the contents of the cache after writing the values to disk
      cacheBuffer.clear();
   }

   public void printData() {
      System.out.println("Cache Contents:");
      //the printing format is as follows:
      //(block-number) (block contents) (dirtyBit value; true/false
      //dirtyBit value is displayed as true if data being queued in the cache
      //is already present in the corresponding blocknum either in cache or the disk
      //and false if there is no redundancy
      for (int loop = 0; loop < cacheBuffer.size(); loop++) {
         System.out.println(cacheBuffer.get(loop).blockNum + " " + new String(cacheBuffer.get(loop).buffer).trim() + " " + cacheBuffer.get(loop).dirtyBit);
      }
   }

   public boolean checkRedundancy() throws IOException {//method to check for redundancy of the data
      //flag is used to indicate the behavior of the dirtyBit value.
      //this is done in order to avoid setting the dirtyBit value from this function
      //and then passing the same value to writeBlock method
      boolean flag = false;
      byte[] buffer = new byte[FileSystem.blockSize];
      if (cacheBuffer.size() > 1) {//indicates that there are at least 2 values present in the cache
         //this check is used to make sure that before checking for redundancy in the cache
         //we make sure that there are 2 elements to check :-)
         for (int loop = cacheBuffer.size() - 1; loop > 0; loop--) {
            if (cacheBuffer.get(cacheBuffer.size() - 1).blockNum == cacheBuffer.get(loop - 1).blockNum
                    && new String(cacheBuffer.get(cacheBuffer.size() - 1).buffer).equals(new String(cacheBuffer.get(loop - 1).buffer))) {
               //set dirtyBit and hence the flag is set to true
               flag = true;
               return flag;
            }
         }
      }
      //check the cache value with the value on the disk for redundancy
      raFile.seek(cacheBuffer.get(cacheBuffer.size() - 1).blockNum * FileSystem.blockSize);
      raFile.read(buffer);
      if (new String(cacheBuffer.get(cacheBuffer.size() - 1).buffer).equals(new String(buffer))) {
         //set flag since we need to set the dirtyBit
         flag = true;
      }
      if (flag) {
         return true;
      } else {
         return false;
      }
   }
}
