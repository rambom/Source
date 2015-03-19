using System;
using System.Collections.Generic;
using System.Text;

namespace TaxPlan.Calculate
{
    public class TaxUtil
    {
        //起征额
        public static decimal taxLimit = 3500;
        //税表
        private static IDictionary<string, PersonalTax> taxRateTable = new Dictionary<string, PersonalTax>()
        {
            {"1",new PersonalTax(){ Level=1, Min=0,Max=1500, Tax=3,Deduct=0}},
            {"2",new PersonalTax(){ Level=2, Min=1500,Max=4500, Tax=10,Deduct=105}},
            {"3",new PersonalTax(){ Level=3, Min=4500,Max=9000, Tax=20,Deduct=555}},
            {"4",new PersonalTax(){ Level=4, Min=9000,Max=35000, Tax=25,Deduct=1005}},
            {"5",new PersonalTax(){ Level=5, Min=35000,Max=55000, Tax=30,Deduct=2775}},
            {"6",new PersonalTax(){ Level=6, Min=55000,Max=80000, Tax=35,Deduct=5505}},
            {"7",new PersonalTax(){ Level=7, Min=80000,Max=int.MaxValue, Tax=45,Deduct=13505}}
        };

        /// <summary>
        /// 获取税档
        /// </summary>
        /// <returns></returns>
        public static PersonalTax GetTaxRateByLevel(int level)
        {
            return taxRateTable[level.ToString()];
        }

        /// <summary>
        /// 获取税率表
        /// </summary>
        /// <returns></returns>
        public static ICollection<PersonalTax> GetTaxRateTable()
        {
            return taxRateTable.Values;
        }

        /// <summary>
        /// 更新税率表
        /// </summary>
        /// <param name="personalTax"></param>
        public static void UpdateTaxRate(PersonalTax personalTax)
        {
            taxRateTable.Add(personalTax.Level.ToString(), personalTax);
        }

        /// <summary>
        ///获取月度工资税档
        /// </summary>
        /// <param name="salary"></param>
        /// <returns></returns>
        public static PersonalTax GetTaxRateBySalary(decimal salary)
        {
            if (salary > taxLimit)
            {
                salary -= taxLimit;
                foreach (KeyValuePair<string, PersonalTax> tax in taxRateTable)
                {
                    if (tax.Value.Min < salary && salary <= tax.Value.Max)
                    { return tax.Value; }
                }
            }
            return new PersonalTax() { Level = 0, Min = 0, Max = taxLimit, Tax = 0, Deduct = 0 };
        }

        /// <summary>
        ///获取年终奖税档
        /// </summary>
        /// <param name="bonus"></param>
        /// <returns></returns>
        public static PersonalTax GetTaxRateByBonus(decimal bonus)
        {
            if (bonus > 0)
            {
                bonus /= 12;
                foreach (KeyValuePair<string, PersonalTax> tax in taxRateTable)
                {
                    if (tax.Value.Min < bonus && bonus <= tax.Value.Max)
                    { return tax.Value; }
                }
            }
            return new PersonalTax() { Level = 0, Min = 0, Max = 0, Tax = 0, Deduct = 0 };
        }

        /// <summary>
        /// 计算月应纳税额
        /// </summary>
        /// <param name="salary"></param>
        /// <returns></returns>
        public static TaxResult CalculateSlaryTax(decimal salary)
        {
            PersonalTax taxRate = GetTaxRateBySalary(salary);
            var tax = (salary - taxLimit) * taxRate.Tax / 100 - taxRate.Deduct;
            return new TaxResult() { Salary = salary, TaxRate = taxRate, TaxSalary = salary - tax, Tax = tax };
        }

        /// <summary>
        /// 计算年终奖应纳税额
        /// </summary>
        /// <param name="bonus"></param>
        /// <returns></returns>
        public static TaxResult CalculateBonusTax(decimal bonus)
        {
            PersonalTax taxRate = GetTaxRateByBonus(bonus);
            var tax = bonus * taxRate.Tax / 100 - taxRate.Deduct;
            return new TaxResult() { Salary = bonus, TaxRate = taxRate, TaxSalary = bonus - tax, Tax = tax };
        }

        /// <summary>
        /// 计算年终终和月薪最佳税率
        /// </summary>
        /// <param name="salary"></param>
        /// <param name="bonus"></param>
        /// <returns></returns>
        public static IDictionary<string, TaxResult> CalculateBestTax(decimal salary, decimal bonus)
        {
            if (salary < taxLimit)
            {
                var limitLess = taxLimit - salary;
                var newSalary = bonus > limitLess ? taxLimit : salary + bonus;
                bonus = salary + bonus - newSalary;
                salary = newSalary;
            }

            var bestBonusTaxResult = CalculateBestTaxOfBonus(salary, bonus);
            var bestSalaryTaxResult = CalculateBestTaxOfSalary(salary, bonus);

            decimal bestBonusTaxSalaryTotal = 0;
            decimal bestSalaryTaxSalaryTotal = 0;

            foreach (var kvp in bestBonusTaxResult)
            {
                bestBonusTaxSalaryTotal += kvp.Value.TaxSalary;
            }

            foreach (var kvp in bestSalaryTaxResult)
            {
                bestSalaryTaxSalaryTotal += kvp.Value.TaxSalary;
            }

            return bestBonusTaxSalaryTotal >= bestSalaryTaxSalaryTotal ? bestBonusTaxResult : bestSalaryTaxResult;
        }

        private static IDictionary<string, TaxResult> CalculateBestTaxOfBonus(decimal salary, decimal bonus)
        {
            var totalSalary = salary + bonus;
            //月税
            var salaryTaxResult = CalculateSlaryTax(salary);
            //年终奖税
            var bonusTaxResult = CalculateBonusTax(bonus);

            for (int i = bonusTaxResult.TaxRate.Level; i >= 1; i--)
            {
                var taxRate = GetTaxRateByLevel(i);
                var bonusLess = taxRate.Max * 12 > bonus ? taxRate.Max * 12 - bonus : 0;

                bonus = salary - taxLimit > bonusLess ? bonus + bonusLess : salary > taxLimit ? bonus + salary - taxLimit : bonus;

                salary = totalSalary - bonus;

                var newSalaryTaxResult = CalculateSlaryTax(salary);

                var newBonusTaxResult = CalculateBonusTax(bonus);

                //税后收入更多
                if (newSalaryTaxResult.TaxSalary + newBonusTaxResult.TaxSalary > salaryTaxResult.TaxSalary + bonusTaxResult.TaxSalary)
                {
                    salaryTaxResult = newSalaryTaxResult;
                    bonusTaxResult = newBonusTaxResult;
                }
            }

            return new Dictionary<string, TaxResult>() { { "salary", salaryTaxResult }, { "bonus", bonusTaxResult } };
        }

        private static IDictionary<string, TaxResult> CalculateBestTaxOfSalary(decimal salary, decimal bonus)
        {
            var totalSalary = salary + bonus;
            //月税
            var salaryTaxResult = CalculateSlaryTax(salary);
            //年终奖税
            var bonusTaxResult = CalculateBonusTax(bonus);

            for (int i = salaryTaxResult.TaxRate.Level; i >= 1; i--)
            {
                var taxRate = GetTaxRateByLevel(i);
                var salaryLess = taxRate.Max + taxLimit > salary ? taxRate.Max + taxLimit - salary : 0;

                if (bonus >= salaryLess)
                {
                    salary = salaryLess > 0 ? salary + salaryLess : taxRate.Max + taxLimit;
                }
                else
                {
                    salary += bonus;
                }

                bonus = totalSalary - salary;

                var newSalaryTaxResult = CalculateSlaryTax(salary);

                var newBonusTaxResult = CalculateBonusTax(bonus);

                //税后收入更多
                if (newSalaryTaxResult.TaxSalary + newBonusTaxResult.TaxSalary > salaryTaxResult.TaxSalary + bonusTaxResult.TaxSalary)
                {
                    salaryTaxResult = newSalaryTaxResult;
                    bonusTaxResult = newBonusTaxResult;
                }
            }

            return new Dictionary<string, TaxResult>() { { "salary", salaryTaxResult }, { "bonus", bonusTaxResult } };
        }
    }
}
