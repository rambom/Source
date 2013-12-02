using System;
using System.Security.Cryptography;
using System.Text;
using Microsoft.SqlServer.Server;

namespace Common.Database.Interface
{
    public class EncryptHelper
    {
        /// <summary>
        /// 默认密码
        /// </summary>
        private const string DefaultPassword = "secretpassword1!";

        /// <summary>
        /// DES3 加密
        /// </summary>
        /// <param name="original">明文</param> 
        /// <returns>返回值</returns>
        [SqlFunction(IsPrecise = true, IsDeterministic = true)]
        public static string Des3Encrypt(string original)
        {
            return EncryptDES(original, DefaultPassword);
        }

        /// <summary>
        /// DES3 解密
        /// </summary>
        /// <param name="original">明文</param>
        /// <returns>返回值</returns>
        [SqlFunction(IsPrecise = true, IsDeterministic = true)]
        public static string Des3Decrypt(string original)
        {
            return DecryptDES(original, DefaultPassword);
        }


        /// <summary>
        /// DES3 加密
        /// </summary>
        /// <param name="original">明文</param>
        /// <param name="password">密钥</param>
        /// <returns>返回值</returns>
        [SqlFunction(IsPrecise = true, IsDeterministic = true)]
        public static string EncryptDES(string original, string password)
        {
            byte[] buffer1 = new MD5CryptoServiceProvider().ComputeHash(Encoding.Unicode.GetBytes(password));
            var provider1 = new TripleDESCryptoServiceProvider { Key = buffer1, Mode = CipherMode.ECB };
            byte[] buffer2 = Encoding.Unicode.GetBytes(original);
            var text1 =
                Convert.ToBase64String(provider1.CreateEncryptor().TransformFinalBlock(buffer2, 0, buffer2.Length));
            return text1;
        }

        /// <summary>
        /// DES3 解密
        /// </summary>
        /// <param name="original">明文</param>
        /// <param name="password">密钥</param>
        /// <returns>返回值</returns>
        [SqlFunction(IsPrecise = true, IsDeterministic = true)]
        public static string DecryptDES(string original, string password)
        {
            byte[] buffer1 = new MD5CryptoServiceProvider().ComputeHash(Encoding.Unicode.GetBytes(password));
            var provider1 = new TripleDESCryptoServiceProvider { Key = buffer1, Mode = CipherMode.ECB };
            byte[] buffer2 = Convert.FromBase64String(original);
            string text1 =
                Encoding.Unicode.GetString(provider1.CreateDecryptor().TransformFinalBlock(buffer2, 0, buffer2.Length));
            return text1;
        }
    }
}
