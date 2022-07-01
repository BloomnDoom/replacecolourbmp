import java.nio.file.{Files, Paths}
import java.io.File
object Main
{
  def main(args:Array[String]):Unit=
  {
    if(args.length!=4)
    {
      print("Usage: [Path to bmp] [Name of output] [hexcode of colour to be replaced]");
      println("[hexcode of replacing colour]");
    }
    else if(invalidHex(args(2))||invalidHex(args(3)))
      println("Enter a valid hexcode, without '#'.");
    else if(!new File(args(0)).exists()||new File(args(0)).isDirectory())
      println("File not found. Enter correct name with extension.");
    else
    {
      val byteArray=Files.readAllBytes(Paths.get(args(0)));
      val imgGroup=byteArray.grouped(3).toArray;
      //First 46 groups of 3 of a bitmap are headers
      val header=imgGroup.slice(0,46);
      //The rest are pixel values which are to be replaced
      val img=imgGroup.slice(46,imgGroup.length);
      val replaced=img.map(replace(_,convert(args(2)),convert(args(3))));
      val newImg=(header++replaced).flatten;
      Files.write(Paths.get(args(1)),newImg);
    }
  }
  def invalidHex(str:String):Boolean=
  {
    val hexChars="0123456789abcdefABCDEF";
    if(str.length!=6) true
    @annotation.tailrec
    def loop(i:Int):Boolean=
    {
      if(i==6) false
      else if(!hexChars.contains(str(i))) true
      else loop(i+1);
    }
    loop(0);
  }
  def convert(str:String):Array[Byte]=
  {
    //Array has to be reversed since RGB values are BGR due to headers
    str.grouped(2).toArray.reverse.map(Integer.parseInt(_,16).toByte);
  }
  def replace(org:Array[Byte],inp:Array[Byte],oup:Array[Byte]):Array[Byte]=
  {
    if(org.sameElements(inp)) oup;
    else org;
  }
}
