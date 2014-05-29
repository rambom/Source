using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using TestStack.White;
using TestStack.White.Factory;
using TestStack.White.UIItems;
using TestStack.White.UIItems.Finders;
using TestStack.White.UIItems.WindowItems;

namespace WhiteUiAutomation
{
    class Program
    {
        static void Main(string[] args)
        {
            test2();
            Console.ReadKey();
        }

        static void test1()
        {
            ProcessStartInfo process = new ProcessStartInfo(@"WindowsFormsApplication1.exe");
            Application app = Application.AttachOrLaunch(process);
            Window window = app.GetWindow("Form1", InitializeOption.NoCache);

            SearchCriteria textSearch = SearchCriteria.ByClassName("WindowsForms10.EDIT.app.0.2bf8098_r16_ad1");
            textSearch.AndControlType(typeof(TextBox), WindowsFramework.WinForms);
            textSearch.AndAutomationId("textbox1");
            TextBox textBox1 = window.Get<TextBox>();

            textBox1.Text = "hello qblong";

            SearchCriteria btnSearch = SearchCriteria.ByClassName("WindowsForms10.BUTTON.app.0.2bf8098_r16_ad1");
            btnSearch.AndControlType(typeof(Button), WindowsFramework.WinForms);
            btnSearch.AndAutomationId("button1");
            Button button = window.Get<Button>(btnSearch);
            button.Click();
        }

        static void test2()
        {
            Application app = Application.Attach("CnEport.SuperPass.UIModule.Eport");
            Window window = app.GetWindow(SearchCriteria.ByAutomationId("AppForm"), InitializeOption.NoCache);

            SearchCriteria searchGName = SearchCriteria.ByClassName("WindowsForms10.EDIT.app.0.202c666");
            searchGName.AndControlType(typeof(TextBox), WindowsFramework.WinForms);
            //searchGName.AndAutomationId("GName");
            //searchGName.AndByText("规格型号");
            StringBuilder sb = new StringBuilder();
            foreach (TextBox a in window.GetMultiple(searchGName))
            {
                sb.AppendFormat("name:{0} id:{1} helpText:{2}\n", a.Name, a.Id, a.HelpText);
                if (a.HelpText.IndexOf("规格型号") > -1)
                {
                    a.Text = "testtest";
                }
            }
            System.IO.File.WriteAllText("textbox.txt", sb.ToString());
        }
    }
}
