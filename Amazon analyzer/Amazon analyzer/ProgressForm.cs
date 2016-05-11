using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Amazon_analyzer
{
    public partial class ProgressForm : Form
    {
        public ProgressForm()
        {
            InitializeComponent();
            this.label1.BackColor = Color.Transparent;
        }
        public bool Increase(int nValue)
        {
            if (nValue < progressBar1.Maximum)
            {
                progressBar1.Value = nValue;
                this.label1.Text = string.Format("{0}%", nValue);
                return true;
            }
            else
            {
                progressBar1.Value = progressBar1.Maximum;
                this.label1.Text = string.Format("{0}%", nValue);
                this.Close();
                return false;
            }
            return false;
        }

    }
}
