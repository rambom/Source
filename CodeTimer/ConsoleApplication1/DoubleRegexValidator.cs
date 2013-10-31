using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace ConsoleApplication1
{
    public class DoubleRegexValidator : IAction
    {
        private string input = "";
        public DoubleRegexValidator()
        { }

        public DoubleRegexValidator(string input)
        {
            this.input = input;
        }
        public void Action()
        {
            bool flag = Regex.IsMatch(input, @"^([0-9])[0-9]*(\.\w*)?$");
        }
    }
}
