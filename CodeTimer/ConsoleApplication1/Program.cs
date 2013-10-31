using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace ConsoleApplication1
{
    class Program
    {
        static void Main(string[] args)
        {
            new IntTest(1000000, "1");
            new DoubleTest(10000, "ss");
            Console.ReadKey();
        }
    }
}
