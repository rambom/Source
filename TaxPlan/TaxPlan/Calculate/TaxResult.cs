using System;
using System.Collections.Generic;
using System.Text;

namespace TaxPlan.Calculate
{
    public class TaxResult
    {
        private decimal salary;
        private PersonalTax taxRate;
        private decimal taxSalary;
        private decimal tax;

        public decimal Salary
        {
            get { return this.salary; }
            set { this.salary = value; }
        }

        public PersonalTax TaxRate
        {
            get { return this.taxRate; }
            set { this.taxRate = value; }
        }
        public decimal TaxSalary
        {
            get { return taxSalary; }
            set { taxSalary = value; }
        }

        public decimal Tax
        {
            get { return tax; }
            set { tax = value; }
        }
    }
}
