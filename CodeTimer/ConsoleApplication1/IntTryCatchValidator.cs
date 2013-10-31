using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication1
{
    public class IntTryCatchValidator : IAction
    {
        private string input = "";
        public IntTryCatchValidator()
        { }

        public IntTryCatchValidator(string input)
        {
            this.input = input;
        }

        public void Action()
        {
            bool flag = true;
            try
            {
                int.Parse(input);
            }
            catch (Exception)
            {
                flag = false;
            }
        }
    }
}
