using System;
using System.Globalization;

namespace Amazon_analyzer.Helpers
{
    /// <summary>
    /// 数值型校验库
    /// </summary>
    public class NumberHelper
    {
        /// <summary>
        /// 是否整数
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="intValue">输出转换值</param>
        /// <returns></returns>
        public static bool IsInteger(object obj, out int intValue)
        {
            intValue = 0;

            if (null != obj)
            {
                return int.TryParse(obj.ToString(), NumberStyles.Integer, null, out intValue);
            }
            return false;
        }

        /// <summary>
        /// 是否正整数
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="uintValue">输出转换值</param>
        /// <returns></returns>
        public static bool IsPositiveInt(object obj, out uint uintValue)
        {
            uintValue = 0;

            if (null != obj)
            {
                return uint.TryParse(obj.ToString(), NumberStyles.Integer, null, out uintValue);
            }
            return false;
        }

        /// <summary>
        /// 指定精度数值
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="integerLen"></param>
        /// <param name="pointLen">小数点长度(0:不允许出现 负数:不验证小数位)</param>
        /// <param name="decValue">输出转换值</param>
        /// <returns></returns>
        public static bool IsNumeric(object obj, uint integerLen, short pointLen, out decimal decValue)
        {
            decValue = decimal.Zero;

            if (null != obj)
            {
                string strValue = obj.ToString();

                //去除尾部多余的0
                if (strValue.IndexOf('.') > -1)
                {
                    strValue = strValue.TrimEnd('0');
                    int length = strValue.LastIndexOf('.') + 4 + pointLen;
                    strValue = strValue.Substring(0, length > strValue.Length ? strValue.Length : length);
                }

                bool blnPass = decimal.TryParse(strValue, NumberStyles.AllowDecimalPoint | NumberStyles.AllowLeadingSign, null, out decValue);

                if (blnPass)
                {
                    decValue = decimal.Round(decValue, pointLen);
                    //最大值,最小值
                    decimal decMax, decMin;

                    //不验证精度
                    if (pointLen < 0)
                    {
                        decMax = Convert.ToDecimal(Math.Pow(10, integerLen)) - 1;

                        decMin = decMax * -1;

                        decValue = decimal.Truncate(decValue);

                        return decValue <= decMax && decValue >= decMin;

                    }

                    decMax = Convert.ToDecimal(Math.Pow(10, integerLen)) - Convert.ToDecimal(Math.Pow(10, pointLen * -1));

                    decMin = decMax * -1;

                    //不允许有精度
                    if (pointLen == 0)
                    {
                        return decValue.ToString().IndexOf('.') == -1 && decValue <= decMax && decValue >= decMin;
                    }

                    //验证精度
                    if (pointLen > 0)
                    {
                        if (decValue <= decMax && decValue >= decMin)
                        {
                            //获取小数位数
                            int intPercision = 0;

                            if (decValue.ToString().IndexOf('.') > -1)
                            {
                                intPercision = decValue.ToString().Length - decValue.ToString().IndexOf('.') - 1;
                            }

                            return pointLen >= intPercision;
                        }
                    }
                }

            }
            return false;
        }

        /// <summary>
        /// 是否百分比数值
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="decValue">输出转换值</param>
        /// <returns></returns>
        public static bool IsPercent(object obj, out decimal decValue)
        {
            decValue = decimal.Zero;
            if (null != obj)
            {
                string strValue = obj.ToString();
                if (strValue.EndsWith("%"))
                {
                    return decimal.TryParse(strValue.TrimEnd('%'), NumberStyles.AllowDecimalPoint | NumberStyles.AllowLeadingSign, null, out decValue);
                }
            }
            return false;
        }
    }
}
