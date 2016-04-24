using Amazon_analyzer.Database;
using Oracle.DataAccess.Client;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Drawing;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Amazon_analyzer
{
    public partial class Form1 : Form
    {
        static string conn_string = ConfigurationManager.ConnectionStrings["db"].ConnectionString;
        int timeout = int.Parse(ConfigurationManager.AppSettings["timeout"]);
        string asinUrl = ConfigurationManager.AppSettings["asin_url"];
        IDataBase db = new OracleHandler(conn_string);
        public Form1()
        {
            InitializeComponent();
            this.textBox1.DoubleClick += this.importFileTextBox_DoubleClick;
            this.textBox2.DoubleClick += this.importFileTextBox_DoubleClick;
            this.textBox3.DoubleClick += this.importFileTextBox_DoubleClick;
            this.textBox4.DoubleClick += this.importFileTextBox_DoubleClick;
            this.textBox5.DoubleClick += this.importFileTextBox_DoubleClick;
            this.textBox6.DoubleClick += this.importFileTextBox_DoubleClick;
            this.textBox7.DoubleClick += this.importFileTextBox_DoubleClick;
            this.textBox8.DoubleClick += this.importFileTextBox_DoubleClick;
            this.linkLabel1.Click += linkLabel1_Click;
            this.linkLabel2.Click += linkLabel2_Click;
            this.linkLabel3.Click += linkLabel3_Click;
            this.linkLabel4.Click += linkLabel4_Click;
            this.linkLabel5.Click += linkLabel5_Click;
            this.linkLabel6.Click += linkLabel6_Click;
            this.linkLabel7.Click += linkLabel7_Click;
            this.linkLabel8.Click += linkLabel8_Click;
            this.backgroundWorker1.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker1.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker1.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.backgroundWorker2.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker2.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker2.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.backgroundWorker3.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker3.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker3.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.backgroundWorker4.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker4.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker4.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.backgroundWorker5.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker5.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker5.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.backgroundWorker6.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker6.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker6.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.backgroundWorker7.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker7.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker7.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.backgroundWorker8.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            this.backgroundWorker8.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker_ProgressChanged);
            this.backgroundWorker8.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker_RunWorkerCompleted);
            this.label1.Text = "";
            this.label2.Text = "";
            this.label3.Text = "";
            this.label4.Text = "";
            this.label5.Text = "";
            this.label6.Text = "";
            this.label7.Text = "";
            this.label8.Text = "";
            this.pager1.EventPaging += pager1_EventPaging;
            this.dataGridView1.AutoGenerateColumns = false;
            this.dataGridView1.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView1.CellContentClick += dataGridView_CellContentClick;
            this.pager2.EventPaging += pager2_EventPaging;
            this.dataGridView2.AutoGenerateColumns = false;
            this.dataGridView2.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView2.CellContentClick += dataGridView_CellContentClick;
            this.pager3.EventPaging += pager3_EventPaging;
            this.dataGridView3.AutoGenerateColumns = false;
            this.dataGridView3.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView3.CellContentClick += dataGridView_CellContentClick;
            this.pager4.EventPaging += pager4_EventPaging;
            this.dataGridView4.AutoGenerateColumns = false;
            this.dataGridView4.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView4.CellContentClick += dataGridView_CellContentClick;
            this.pager5.EventPaging += pager5_EventPaging;
            this.dataGridView5.AutoGenerateColumns = false;
            this.dataGridView5.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView5.CellContentClick += dataGridView_CellContentClick;
            this.pager6.EventPaging += pager6_EventPaging;
            this.dataGridView6.AutoGenerateColumns = false;
            this.dataGridView6.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView6.CellContentClick += dataGridView_CellContentClick;
            this.pager7.EventPaging += pager7_EventPaging;
            this.dataGridView7.AutoGenerateColumns = false;
            this.dataGridView7.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView7.CellContentClick += dataGridView_CellContentClick;
            this.pager8.EventPaging += pager8_EventPaging;
            this.dataGridView8.AutoGenerateColumns = false;
            this.dataGridView8.RowPostPaint += dataGridView_RowPostPaint;
            this.dataGridView8.CellContentClick += dataGridView_CellContentClick;
        }

        int pager1_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["top_asin_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox9.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label9.Text);
                param.Add(new DataParameter(label9.Text, DbType.String, 50, textBox9.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox10.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label10.Text);
                param.Add(new DataParameter(label10.Text, DbType.String, 50, textBox10.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox11.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label11.Text);
                param.Add(new DataParameter(label11.Text, DbType.String, 50, textBox11.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox12.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label12.Text);
                param.Add(new DataParameter(label12.Text, DbType.String, 50, textBox12.Text.Trim()));
            }

            if (!string.IsNullOrEmpty(textBox43.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label43.Text);
                param.Add(new DataParameter(label43.Text, DbType.String, 50, textBox43.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox42.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label42.Text);
                param.Add(new DataParameter(label42.Text, DbType.String, 50, textBox42.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager1.PageCurrent - 1) * this.pager1.PageSize, (this.pager1.PageCurrent) * this.pager1.PageSize);
            this.dataGridView1.DataSource = dt;
            return (int)rowsCount;
        }

        int pager2_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["top_seller_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox13.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label13.Text);
                param.Add(new DataParameter(label13.Text, DbType.String, 50, textBox13.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox14.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label14.Text);
                param.Add(new DataParameter(label14.Text, DbType.String, 50, textBox14.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox15.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label15.Text);
                param.Add(new DataParameter(label15.Text, DbType.String, 50, textBox15.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox16.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label16.Text);
                param.Add(new DataParameter(label16.Text, DbType.String, 50, textBox16.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager2.PageCurrent - 1) * this.pager1.PageSize, (this.pager2.PageCurrent) * this.pager2.PageSize);
            this.dataGridView2.DataSource = dt;
            return (int)rowsCount;
        }
        int pager3_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["top_brand_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox17.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label17.Text);
                param.Add(new DataParameter(label17.Text, DbType.String, 50, textBox17.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox18.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label18.Text);
                param.Add(new DataParameter(label18.Text, DbType.String, 50, textBox18.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager3.PageCurrent - 1) * this.pager3.PageSize, (this.pager3.PageCurrent) * this.pager3.PageSize);
            this.dataGridView3.DataSource = dt;
            return (int)rowsCount;
        }
        int pager4_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["top_subcategory_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox19.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label19.Text);
                param.Add(new DataParameter(label19.Text, DbType.String, 50, textBox19.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox20.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label20.Text);
                param.Add(new DataParameter(label20.Text, DbType.String, 50, textBox20.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox21.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label21.Text);
                param.Add(new DataParameter(label21.Text, DbType.String, 50, textBox21.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager4.PageCurrent - 1) * this.pager4.PageSize, (this.pager4.PageCurrent) * this.pager4.PageSize);
            this.dataGridView4.DataSource = dt;
            return (int)rowsCount;
        }
        int pager5_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["mover_shaker_asin_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox22.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label22.Text);
                param.Add(new DataParameter(label22.Text, DbType.String, 50, textBox22.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox23.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label23.Text);
                param.Add(new DataParameter(label23.Text, DbType.String, 50, textBox23.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox24.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label24.Text);
                param.Add(new DataParameter(label24.Text, DbType.String, 50, textBox24.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox25.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label25.Text);
                param.Add(new DataParameter(label25.Text, DbType.String, 50, textBox25.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox26.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label26.Text);
                param.Add(new DataParameter(label26.Text, DbType.String, 50, textBox26.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox27.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label27.Text);
                param.Add(new DataParameter(label27.Text, DbType.String, 50, textBox27.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager5.PageCurrent - 1) * this.pager5.PageSize, (this.pager5.PageCurrent) * this.pager5.PageSize);
            this.dataGridView5.DataSource = dt;
            return (int)rowsCount;
        }
        int pager6_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["mover_shaker_brand_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox28.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label28.Text);
                param.Add(new DataParameter(label28.Text, DbType.String, 50, textBox28.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox29.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label29.Text);
                param.Add(new DataParameter(label29.Text, DbType.String, 50, textBox29.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager6.PageCurrent - 1) * this.pager6.PageSize, (this.pager6.PageCurrent) * this.pager6.PageSize);
            this.dataGridView6.DataSource = dt;
            return (int)rowsCount;
        }
        int pager7_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["top_asin_with_limited_match_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox31.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label31.Text);
                param.Add(new DataParameter(label31.Text, DbType.String, 50, textBox31.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox32.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label32.Text);
                param.Add(new DataParameter(label32.Text, DbType.String, 50, textBox32.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox34.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label34.Text);
                param.Add(new DataParameter(label34.Text, DbType.String, 50, textBox34.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox33.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label33.Text);
                param.Add(new DataParameter(label33.Text, DbType.String, 50, textBox33.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox35.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label35.Text);
                param.Add(new DataParameter(label35.Text, DbType.String, 50, textBox35.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox30.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label30.Text);
                param.Add(new DataParameter(label30.Text, DbType.String, 50, textBox30.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager7.PageCurrent - 1) * this.pager7.PageSize, (this.pager7.PageCurrent) * this.pager7.PageSize);
            this.dataGridView7.DataSource = dt;
            return (int)rowsCount;
        }
        int pager8_EventPaging(Control.EventPagingArg e)
        {

            string strCondition = "";
            string tableName = ConfigurationManager.AppSettings["top_conversion_rate_table"];
            List<DataParameter> param = new List<DataParameter>();
            if (!string.IsNullOrEmpty(textBox37.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label37.Text);
                param.Add(new DataParameter(label37.Text, DbType.String, 50, textBox37.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox38.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label38.Text);
                param.Add(new DataParameter(label38.Text, DbType.String, 50, textBox38.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox40.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label40.Text);
                param.Add(new DataParameter(label40.Text, DbType.String, 50, textBox40.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox39.Text))
            {
                strCondition += string.Format(" and {0}=:{0}", this.label39.Text);
                param.Add(new DataParameter(label39.Text, DbType.String, 50, textBox39.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox41.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label41.Text);
                param.Add(new DataParameter(label41.Text, DbType.String, 50, textBox41.Text.Trim()));
            }
            if (!string.IsNullOrEmpty(textBox36.Text))
            {
                strCondition += string.Format(" and {0} like '%' || :{0} || '%'", this.label36.Text);
                param.Add(new DataParameter(label36.Text, DbType.String, 50, textBox36.Text.Trim()));
            }

            decimal rowsCount = db.ExecuteScalar("select count(1) from " + tableName + " where 1=1 " + strCondition, param);
            DataTable dt = db.ExecuteDataTablePaged(tableName, "*", "", strCondition, param, "", (this.pager8.PageCurrent - 1) * this.pager8.PageSize, (this.pager8.PageCurrent) * this.pager8.PageSize);
            this.dataGridView8.DataSource = dt;
            return (int)rowsCount;
        }
        ~Form1()
        {
            if (db != null)
                db.CloseConn();

        }

        private void importFileTextBox_DoubleClick(object sender, EventArgs e)
        {
            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                ((TextBox)sender).Text = string.Join(";", openFileDialog.FileNames);

            }
        }

        private void linkLabel1_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox1.Text.Split(';');
            this.label1.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox1.Enabled = false;
            this.linkLabel1.Enabled = false;
            object[] para = new object[2];
            para[0] = 0;
            para[1] = files;
            this.backgroundWorker1.RunWorkerAsync(para);
        }
        private void linkLabel2_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox2.Text.Split(';');
            this.label2.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox2.Enabled = false;
            this.linkLabel2.Enabled = false;
            object[] para = new object[2];
            para[0] = 1;
            para[1] = files;
            this.backgroundWorker2.RunWorkerAsync(para);
        }
        private void linkLabel3_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox3.Text.Split(';');
            this.label3.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox3.Enabled = false;
            this.linkLabel3.Enabled = false;
            object[] para = new object[2];
            para[0] = 2;
            para[1] = files;
            this.backgroundWorker3.RunWorkerAsync(para);
        }
        private void linkLabel4_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox4.Text.Split(';');
            this.label4.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox4.Enabled = false;
            this.linkLabel4.Enabled = false;
            object[] para = new object[2];
            para[0] = 3;
            para[1] = files;
            this.backgroundWorker4.RunWorkerAsync(para);
        }
        private void linkLabel5_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox5.Text.Split(';');
            this.label5.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox5.Enabled = false;
            this.linkLabel5.Enabled = false;
            object[] para = new object[2];
            para[0] = 4;
            para[1] = files;
            this.backgroundWorker5.RunWorkerAsync(para);
        }
        private void linkLabel6_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox6.Text.Split(';');
            this.label6.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox6.Enabled = false;
            this.linkLabel6.Enabled = false;
            object[] para = new object[2];
            para[0] = 5;
            para[1] = files;
            this.backgroundWorker6.RunWorkerAsync(para);
        }
        private void linkLabel7_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox7.Text.Split(';');
            this.label7.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox7.Enabled = false;
            this.linkLabel7.Enabled = false;
            object[] para = new object[2];
            para[0] = 6;
            para[1] = files;
            this.backgroundWorker7.RunWorkerAsync(para);
        }
        private void linkLabel8_Click(object sender, EventArgs e)
        {
            string[] files = this.textBox8.Text.Split(';');
            this.label8.Text = "";

            foreach (string path in files)
            {
                if (!File.Exists(path))
                {
                    MessageBox.Show(string.Format("文件[{0}]不存在.", path));
                    return;
                }
            }
            this.textBox8.Enabled = false;
            this.linkLabel8.Enabled = false;
            object[] para = new object[2];
            para[0] = 7;
            para[1] = files;
            this.backgroundWorker8.RunWorkerAsync(para);
        }
        private void backgroundWorker_DoWork(object sender, DoWorkEventArgs e)
        {
            object[] para = (object[])e.Argument;
            int tabInx = (int)para[0];
            string[] files = (string[])para[1];
            string strHead = "";
            string strDelimited = "";
            string strMap = "";
            string strTable = "";
            bool clear = true;

            switch (tabInx)
            {
                case 0:
                    strHead = ConfigurationManager.AppSettings["top_asin_head"];
                    strDelimited = ConfigurationManager.AppSettings["top_asin_delimited"];
                    strMap = ConfigurationManager.AppSettings["top_asin_map"];
                    strTable = ConfigurationManager.AppSettings["top_asin_table"];
                    clear = this.checkBox1.Checked;
                    break;
                case 1:
                    strHead = ConfigurationManager.AppSettings["top_seller_head"];
                    strDelimited = ConfigurationManager.AppSettings["top_seller_delimited"];
                    strMap = ConfigurationManager.AppSettings["top_seller_map"];
                    strTable = ConfigurationManager.AppSettings["top_seller_table"];
                    clear = this.checkBox2.Checked;
                    break;
                case 2:
                    strHead = ConfigurationManager.AppSettings["top_brand_head"];
                    strDelimited = ConfigurationManager.AppSettings["top_brand_delimited"];
                    strMap = ConfigurationManager.AppSettings["top_brand_map"];
                    strTable = ConfigurationManager.AppSettings["top_brand_table"];
                    clear = this.checkBox3.Checked;
                    break;
                case 3:
                    strHead = ConfigurationManager.AppSettings["top_subcategory_head"];
                    strDelimited = ConfigurationManager.AppSettings["top_subcategory_delimited"];
                    strMap = ConfigurationManager.AppSettings["top_subcategory_map"];
                    strTable = ConfigurationManager.AppSettings["top_subcategory_table"];
                    clear = this.checkBox4.Checked;
                    break;
                case 4:
                    strHead = ConfigurationManager.AppSettings["mover_shaker_asin_head"];
                    strDelimited = ConfigurationManager.AppSettings["mover_shaker_asin_delimited"];
                    strMap = ConfigurationManager.AppSettings["mover_shaker_asin_map"];
                    strTable = ConfigurationManager.AppSettings["mover_shaker_asin_table"];
                    clear = this.checkBox5.Checked;
                    break;
                case 5:
                    strHead = ConfigurationManager.AppSettings["mover_shaker_brand_head"];
                    strDelimited = ConfigurationManager.AppSettings["mover_shaker_brand_delimited"];
                    strMap = ConfigurationManager.AppSettings["mover_shaker_brand_map"];
                    strTable = ConfigurationManager.AppSettings["mover_shaker_brand_table"];
                    clear = this.checkBox6.Checked;
                    break;
                case 6:
                    strHead = ConfigurationManager.AppSettings["top_asin_with_limited_match_head"];
                    strDelimited = ConfigurationManager.AppSettings["top_asin_with_limited_match_delimited"];
                    strMap = ConfigurationManager.AppSettings["top_asin_with_limited_match_map"];
                    strTable = ConfigurationManager.AppSettings["top_asin_with_limited_match_table"];
                    clear = this.checkBox7.Checked;
                    break;
                case 7:
                    strHead = ConfigurationManager.AppSettings["top_conversion_rate_head"];
                    strDelimited = ConfigurationManager.AppSettings["top_conversion_rate_delimited"];
                    strMap = ConfigurationManager.AppSettings["top_conversion_rate_map"];
                    strTable = ConfigurationManager.AppSettings["top_conversion_rate_table"];
                    clear = this.checkBox8.Checked;
                    break;
            }
            ImportTxt((BackgroundWorker)sender, tabInx, clear, files, strHead, strDelimited, strMap, strTable);
            e.Result = tabInx;
        }

        private void backgroundWorker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            if (e.ProgressPercentage < 0)
            {
                MessageBox.Show(e.UserState.ToString());
            }
            else
            {
                switch (e.ProgressPercentage)
                {
                    case 0:
                        this.label1.Text = e.UserState.ToString();
                        break;
                    case 1:
                        this.label2.Text = e.UserState.ToString();
                        break;
                    case 2:
                        this.label3.Text = e.UserState.ToString();
                        break;
                    case 3:
                        this.label4.Text = e.UserState.ToString();
                        break;
                    case 4:
                        this.label5.Text = e.UserState.ToString();
                        break;
                    case 5:
                        this.label6.Text = e.UserState.ToString();
                        break;
                    case 6:
                        this.label7.Text = e.UserState.ToString();
                        break;
                    case 7:
                        this.label8.Text = e.UserState.ToString();
                        break;
                }
            }
        }

        private void backgroundWorker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            switch ((int)e.Result)
            {
                case 0:
                    this.textBox1.Enabled = true;
                    this.linkLabel1.Enabled = true;
                    break;
                case 1:
                    this.textBox2.Enabled = true;
                    this.linkLabel2.Enabled = true;
                    break;
                case 2:
                    this.textBox3.Enabled = true;
                    this.linkLabel3.Enabled = true;
                    break;
                case 3:
                    this.textBox4.Enabled = true;
                    this.linkLabel4.Enabled = true;
                    break;
                case 4:
                    this.textBox5.Enabled = true;
                    this.linkLabel5.Enabled = true;
                    break;
                case 5:
                    this.textBox6.Enabled = true;
                    this.linkLabel6.Enabled = true;
                    break;
                case 6:
                    this.textBox7.Enabled = true;
                    this.linkLabel7.Enabled = true;
                    break;
                case 7:
                    this.textBox8.Enabled = true;
                    this.linkLabel8.Enabled = true;
                    break;
            }
        }
        private void ImportTxt(BackgroundWorker sender, int tabInx, bool clear, string[] files, string head, string delimited, string colMap, string table)
        {
            int totalRows = 0;
            long totalTime = 0;

            if (clear)
            {
                db.ExecuteNoQuery(string.Format("truncate table {0}", table), null);
            }

            foreach (string path in files)
            {
                sender.ReportProgress(tabInx, string.Format("当前文件：{0}", Path.GetFileName(path)));
                string strErr = string.Empty;
                int importRows = 0;
                long spentTime = 0;

                Dictionary<string, string> columnMap = new Dictionary<string, string>();
                foreach (string str in colMap.Split(';'))
                {
                    var arr = str.Split(',');
                    columnMap.Add(arr[0], arr[1]);
                }

                bool flag = db.FastImportTextToDb(conn_string, 1000, timeout, path, head, delimited, table, columnMap, null, 2, "", out importRows, out spentTime, out strErr);

                totalRows += importRows;
                totalTime += spentTime;
                if (flag)
                {
                    sender.ReportProgress(tabInx, string.Format("已处理{0}条数据，用时{1}秒", totalRows
                        , totalTime / 1000));
                }
                else
                {
                    sender.ReportProgress(-1, string.Format("导入失败,已处理{0}条数据：{1}", totalRows, strErr));
                }
            }
        }

        private void linkLabel9_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager1.Bind();
        }

        private void dataGridView_RowPostPaint(object sender, DataGridViewRowPostPaintEventArgs e)
        {
            using (SolidBrush b = new SolidBrush(((DataGridView)sender).RowHeadersDefaultCellStyle.ForeColor))
            {
                e.Graphics.DrawString(Convert.ToString(e.RowIndex + 1, CultureInfo.CurrentUICulture),
                e.InheritedRowStyle.Font, b, e.RowBounds.Location.X + 10, e.RowBounds.Location.Y + 4);
            }
        }

        private void linkLabel10_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager2.Bind();
        }

        private void linkLabel11_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager3.Bind();
        }

        private void linkLabel12_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager4.Bind();
        }

        private void linkLabel13_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager5.Bind();
        }

        private void linkLabel14_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager6.Bind();
        }

        private void linkLabel15_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager7.Bind();
        }

        private void linkLabel16_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.pager8.Bind();
        }
        private void dataGridView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (((DataGridView)sender).Columns[e.ColumnIndex].DataPropertyName == "ASIN")
            {
                System.Diagnostics.Process.Start(string.Format("{0}{1}", asinUrl, ((DataGridView)sender).Rows[e.RowIndex].Cells[e.ColumnIndex].Value.ToString()));
            }
        }
    }
}
