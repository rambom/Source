namespace CrackEmail
{
    partial class Form1
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.txtSmtp = new System.Windows.Forms.TextBox();
            this.txtMail = new System.Windows.Forms.TextBox();
            this.txtDict = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.lbDict = new System.Windows.Forms.Label();
            this.lbMail = new System.Windows.Forms.Label();
            this.btnRun = new System.Windows.Forms.Button();
            this.lbSmtp = new System.Windows.Forms.Label();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // txtSmtp
            // 
            this.txtSmtp.Location = new System.Drawing.Point(62, 35);
            this.txtSmtp.Name = "txtSmtp";
            this.txtSmtp.Size = new System.Drawing.Size(164, 21);
            this.txtSmtp.TabIndex = 0;
            // 
            // txtMail
            // 
            this.txtMail.Location = new System.Drawing.Point(62, 90);
            this.txtMail.Name = "txtMail";
            this.txtMail.Size = new System.Drawing.Size(164, 21);
            this.txtMail.TabIndex = 1;
            // 
            // txtDict
            // 
            this.txtDict.Location = new System.Drawing.Point(62, 141);
            this.txtDict.Name = "txtDict";
            this.txtDict.ReadOnly = true;
            this.txtDict.Size = new System.Drawing.Size(164, 21);
            this.txtDict.TabIndex = 2;
            this.txtDict.Text = "单击选择文件";
            this.txtDict.Click += new System.EventHandler(this.txtDict_Click);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.lbDict);
            this.groupBox1.Controls.Add(this.lbMail);
            this.groupBox1.Controls.Add(this.btnRun);
            this.groupBox1.Controls.Add(this.lbSmtp);
            this.groupBox1.Controls.Add(this.txtSmtp);
            this.groupBox1.Controls.Add(this.txtDict);
            this.groupBox1.Controls.Add(this.txtMail);
            this.groupBox1.Location = new System.Drawing.Point(12, 22);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(268, 229);
            this.groupBox1.TabIndex = 3;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Info";
            // 
            // lbDict
            // 
            this.lbDict.AutoSize = true;
            this.lbDict.Location = new System.Drawing.Point(21, 144);
            this.lbDict.Name = "lbDict";
            this.lbDict.Size = new System.Drawing.Size(35, 12);
            this.lbDict.TabIndex = 6;
            this.lbDict.Text = "Dict:";
            // 
            // lbMail
            // 
            this.lbMail.AutoSize = true;
            this.lbMail.Location = new System.Drawing.Point(21, 90);
            this.lbMail.Name = "lbMail";
            this.lbMail.Size = new System.Drawing.Size(35, 12);
            this.lbMail.TabIndex = 5;
            this.lbMail.Text = "User:";
            // 
            // btnRun
            // 
            this.btnRun.Location = new System.Drawing.Point(89, 184);
            this.btnRun.Name = "btnRun";
            this.btnRun.Size = new System.Drawing.Size(75, 23);
            this.btnRun.TabIndex = 4;
            this.btnRun.Text = "Run";
            this.btnRun.UseVisualStyleBackColor = true;
            this.btnRun.Click += new System.EventHandler(this.btnRun_Click);
            // 
            // lbSmtp
            // 
            this.lbSmtp.AutoSize = true;
            this.lbSmtp.Location = new System.Drawing.Point(9, 38);
            this.lbSmtp.Name = "lbSmtp";
            this.lbSmtp.Size = new System.Drawing.Size(47, 12);
            this.lbSmtp.TabIndex = 3;
            this.lbSmtp.Text = "Server:";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(292, 273);
            this.Controls.Add(this.groupBox1);
            this.Name = "Form1";
            this.Text = "Email Crack by qblong";
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TextBox txtSmtp;
        private System.Windows.Forms.TextBox txtMail;
        private System.Windows.Forms.TextBox txtDict;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label lbDict;
        private System.Windows.Forms.Label lbMail;
        private System.Windows.Forms.Button btnRun;
        private System.Windows.Forms.Label lbSmtp;
    }
}

