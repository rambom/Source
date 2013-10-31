using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication1
{
    public class DoubleTryParseValidator : IAction
    {
        private string input = "";
        public DoubleTryParseValidator()
        { }

        public DoubleTryParseValidator(string input)
        {
            this.input = input;
        }

        public void Action()
        {
            double i;
            bool flag = double.TryParse(input, out i);
        }
    }
}
