using System;
using System.Collections.Generic;
using System.Text;

namespace TaxPlan.Calculate
{
    public class PersonalTax
    {
        int level;

        public int Level
        {
            get { return level; }
            set { level = value; }
        }
        decimal min;

        public decimal Min
        {
            get { return min; }
            set { min = value; }
        }
        decimal max;

        public decimal Max
        {
            get { return max; }
            set { max = value; }
        }
        decimal tax;

        public decimal Tax
        {
            get { return tax; }
            set { tax = value; }
        }
        decimal deduct;

        public decimal Deduct
        {
            get { return deduct; }
            set { deduct = value; }
        }
    }
}
