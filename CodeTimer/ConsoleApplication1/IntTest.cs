using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication1
{
    public class IntTest
    {
        public IntTest(int iteration, string input)
        {
            CodeTimer.Time("IntRegexValidator", iteration, new IntRegexValidator(input));
            CodeTimer.Time("IntTryParseValidator", iteration, new IntTryParseValidator(input));
            CodeTimer.Time("IntTryCatchValidator", iteration, new IntTryCatchValidator(input));
        }
        public IntTest()
        {

        }
    }
}
