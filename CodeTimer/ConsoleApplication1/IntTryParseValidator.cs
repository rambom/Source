using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication1
{
    public class IntTryParseValidator : IAction
    {
        private string input = "";
        public IntTryParseValidator()
        { }

        public IntTryParseValidator(string input)
        {
            this.input = input;
        }

        public void Action()
        {
            int i;
            bool flag = int.TryParse(input, out i);
        }
    }
}
