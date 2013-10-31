using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplication1
{
    public class DoubleTryCatchValidator : IAction
    {
        private string input = "";
        public DoubleTryCatchValidator()
        { }

        public DoubleTryCatchValidator(string input)
        {
            this.input = input;
        }

        public void Action()
        {
            bool flag = true;
            try
            {
                Double.Parse(input);
            }
            catch (Exception)
            {
                flag = false;
            }
        }
    }
}
