namespace CSV_Split
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
        /// 设计器支持所需的方法 - 不要修改
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.txtFilePath = new System.Windows.Forms.TextBox();
            this.txtRowCount = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.btnDoing = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.FileName = "openFileDialog1";
            // 
            // txtFilePath
            // 
            this.txtFilePath.BackColor = System.Drawing.SystemColors.Window;
            this.txtFilePath.ForeColor = System.Drawing.SystemColors.GrayText;
            this.txtFilePath.HideSelection = false;
            this.txtFilePath.Location = new System.Drawing.Point(79, 33);
            this.txtFilePath.Name = "txtFilePath";
            this.txtFilePath.ReadOnly = true;
            this.txtFilePath.Size = new System.Drawing.Size(228, 21);
            this.txtFilePath.TabIndex = 1;
            this.txtFilePath.Text = "双击选择文件...";
            this.txtFilePath.Enter += new System.EventHandler(this.txtFilePath_Enter);
            this.txtFilePath.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.txtFilePath_MouseDoubleClick);
            // 
            // txtRowCount
            // 
            this.txtRowCount.Cursor = System.Windows.Forms.Cursors.IBeam;
            this.txtRowCount.Location = new System.Drawing.Point(79, 66);
            this.txtRowCount.Name = "txtRowCount";
            this.txtRowCount.Size = new System.Drawing.Size(228, 21);
            this.txtRowCount.TabIndex = 1;
            this.txtRowCount.Text = "1,000,000";
            this.txtRowCount.TextChanged += new System.EventHandler(this.textBox2_TextChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(22, 36);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(53, 12);
            this.label1.TabIndex = 0;
            this.label1.Text = "文件路径";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(22, 69);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(53, 12);
            this.label2.TabIndex = 2;
            this.label2.Text = "折分行数";
            // 
            // btnDoing
            // 
            this.btnDoing.Location = new System.Drawing.Point(79, 106);
            this.btnDoing.Name = "btnDoing";
            this.btnDoing.Size = new System.Drawing.Size(75, 23);
            this.btnDoing.TabIndex = 0;
            this.btnDoing.Text = "Split";
            this.btnDoing.UseVisualStyleBackColor = true;
            this.btnDoing.Click += new System.EventHandler(this.btnDoing_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(336, 141);
            this.Controls.Add(this.btnDoing);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.txtRowCount);
            this.Controls.Add(this.txtFilePath);
            this.DoubleBuffered = true;
            this.Name = "Form1";
            this.Text = "CSV Split";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.Windows.Forms.TextBox txtFilePath;
        private System.Windows.Forms.TextBox txtRowCount;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnDoing;
    }
}

