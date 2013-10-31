namespace CrackUrtracker
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
            System.Windows.Forms.Button btnCrack;
            this.txtUrl = new System.Windows.Forms.TextBox();
            this.gbPack = new System.Windows.Forms.GroupBox();
            this.cbVer = new System.Windows.Forms.ComboBox();
            this.lbUsers = new System.Windows.Forms.Label();
            this.lbVer = new System.Windows.Forms.Label();
            this.lbSetupUrl = new System.Windows.Forms.Label();
            this.txtUsers = new System.Windows.Forms.TextBox();
            this.txtMachine = new System.Windows.Forms.TextBox();
            this.lbMachine = new System.Windows.Forms.Label();
            btnCrack = new System.Windows.Forms.Button();
            this.gbPack.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnCrack
            // 
            btnCrack.Location = new System.Drawing.Point(93, 193);
            btnCrack.Name = "btnCrack";
            btnCrack.Size = new System.Drawing.Size(75, 23);
            btnCrack.TabIndex = 2;
            btnCrack.Text = "Crack";
            btnCrack.UseVisualStyleBackColor = true;
            btnCrack.Click += new System.EventHandler(this.btnCrack_Click);
            // 
            // txtUrl
            // 
            this.txtUrl.Location = new System.Drawing.Point(93, 29);
            this.txtUrl.Name = "txtUrl";
            this.txtUrl.ReadOnly = true;
            this.txtUrl.Size = new System.Drawing.Size(151, 21);
            this.txtUrl.TabIndex = 0;
            this.txtUrl.Text = "单击选择路径";
            this.txtUrl.Click += new System.EventHandler(this.txtUrl_Click);
            // 
            // gbPack
            // 
            this.gbPack.Controls.Add(this.cbVer);
            this.gbPack.Controls.Add(this.lbMachine);
            this.gbPack.Controls.Add(this.lbUsers);
            this.gbPack.Controls.Add(this.lbVer);
            this.gbPack.Controls.Add(this.lbSetupUrl);
            this.gbPack.Controls.Add(this.txtMachine);
            this.gbPack.Controls.Add(this.txtUsers);
            this.gbPack.Controls.Add(btnCrack);
            this.gbPack.Controls.Add(this.txtUrl);
            this.gbPack.Location = new System.Drawing.Point(12, 26);
            this.gbPack.Name = "gbPack";
            this.gbPack.Size = new System.Drawing.Size(280, 237);
            this.gbPack.TabIndex = 1;
            this.gbPack.TabStop = false;
            this.gbPack.Text = "Pack";
            // 
            // cbVer
            // 
            this.cbVer.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbVer.FormattingEnabled = true;
            this.cbVer.Location = new System.Drawing.Point(93, 68);
            this.cbVer.Name = "cbVer";
            this.cbVer.Size = new System.Drawing.Size(151, 20);
            this.cbVer.TabIndex = 5;
            // 
            // lbUsers
            // 
            this.lbUsers.AutoSize = true;
            this.lbUsers.Location = new System.Drawing.Point(34, 116);
            this.lbUsers.Name = "lbUsers";
            this.lbUsers.Size = new System.Drawing.Size(53, 12);
            this.lbUsers.TabIndex = 4;
            this.lbUsers.Text = "用户数：";
            // 
            // lbVer
            // 
            this.lbVer.AutoSize = true;
            this.lbVer.Location = new System.Drawing.Point(34, 68);
            this.lbVer.Name = "lbVer";
            this.lbVer.Size = new System.Drawing.Size(53, 12);
            this.lbVer.TabIndex = 1;
            this.lbVer.Text = "版本号：";
            // 
            // lbSetupUrl
            // 
            this.lbSetupUrl.AutoSize = true;
            this.lbSetupUrl.Location = new System.Drawing.Point(22, 32);
            this.lbSetupUrl.Name = "lbSetupUrl";
            this.lbSetupUrl.Size = new System.Drawing.Size(65, 12);
            this.lbSetupUrl.TabIndex = 1;
            this.lbSetupUrl.Text = "安装路径：";
            // 
            // txtUsers
            // 
            this.txtUsers.ImeMode = System.Windows.Forms.ImeMode.Off;
            this.txtUsers.Location = new System.Drawing.Point(93, 113);
            this.txtUsers.MaxLength = 5;
            this.txtUsers.Name = "txtUsers";
            this.txtUsers.Size = new System.Drawing.Size(151, 21);
            this.txtUsers.TabIndex = 3;
            this.txtUsers.Text = "1000";
            // 
            // txtMachine
            // 
            this.txtMachine.ImeMode = System.Windows.Forms.ImeMode.Off;
            this.txtMachine.Location = new System.Drawing.Point(93, 155);
            this.txtMachine.MaxLength = 8;
            this.txtMachine.Name = "txtMachine";
            this.txtMachine.Size = new System.Drawing.Size(151, 21);
            this.txtMachine.TabIndex = 3;
            // 
            // lbMachine
            // 
            this.lbMachine.AutoSize = true;
            this.lbMachine.Location = new System.Drawing.Point(34, 158);
            this.lbMachine.Name = "lbMachine";
            this.lbMachine.Size = new System.Drawing.Size(53, 12);
            this.lbMachine.TabIndex = 4;
            this.lbMachine.Text = "机器码：";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(304, 295);
            this.Controls.Add(this.gbPack);
            this.Name = "Form1";
            this.Text = "UrTracker Crack by qblong";
            this.gbPack.ResumeLayout(false);
            this.gbPack.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TextBox txtUrl;
        private System.Windows.Forms.GroupBox gbPack;
        private System.Windows.Forms.Label lbSetupUrl;
        private System.Windows.Forms.Label lbUsers;
        private System.Windows.Forms.TextBox txtUsers;
        private System.Windows.Forms.Label lbVer;
        private System.Windows.Forms.ComboBox cbVer;
        private System.Windows.Forms.Label lbMachine;
        private System.Windows.Forms.TextBox txtMachine;
    }
}

