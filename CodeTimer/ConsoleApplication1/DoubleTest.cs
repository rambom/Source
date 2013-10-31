using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication1
{
    public class DoubleTest
    {
        public DoubleTest(int iteration, string input)
        {
            CodeTimer.Time("DoubleRegexValidator", iteration, new DoubleRegexValidator(input));
            CodeTimer.Time("DoubleTryParseValidator", iteration, new DoubleTryParseValidator(input));
            CodeTimer.Time("DoubleTryCatchValidator", iteration, new DoubleTryCatchValidator(input));
        }
        public DoubleTest()
        {

        }
    }
}
