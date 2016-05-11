using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using System.Data.OleDb;
using System.IO;
using Aspose.Cells;

namespace Amazon_analyzer.Helpers
{
    /// <summary>
    /// 对数据的导入、导出类
    /// </summary>
    public class ExcelHelper
    {
        /// <summary>
        /// 列头
        /// </summary>
        private const string HeadColumn = "HeadColumn";
        /// <summary>
        /// 列尾
        /// </summary>
        private const string FootColumn = "FootColumn";
        /// <summary>
        /// EXCEL工作表最大存放的行数
        /// </summary>
        private const int IntPageSize = 65530;

        #region excel导出
        #region private 方法 获取sheet个数、添加行
        /// <summary>
        /// 获取sheet的个数
        /// </summary>
        /// <param name="intRowCount">总行数</param>
        /// <param name="intRows">每sheet的行数</param>
        /// <returns>返回值</returns>
        private static int GetSheetCount(int intRowCount, int intRows)
        {
            int n = intRowCount % intRows;		//余数
            if (n == 0)
                return intRowCount / intRows;
            else
                return Convert.ToInt32(intRowCount / intRows) + 1;
        }

        /// <summary>
        /// 给datatable添加行
        /// </summary>
        /// <param name="sender">object sender</param>
        /// <param name="args">args</param>
        private static void DataTableRowChanged(object sender, DataRowChangeEventArgs args)
        {
            args.Row.SetAdded();
        }
        #endregion

        /// <summary>
        /// 将数据源传入，根据设定的导入范围，返回要插入到当前sheet的数据
        /// </summary>
        /// <param name="dt">要插入到excel的数据源</param>
        /// <param name="intSheetLoge">操作excel第几个sheet</param>
        /// <param name="intSheetCount">一共要分几个sheet</param>
        /// <param name="intRowMax">当前sheet最多插入多少行记录</param>
        /// <param name="intDataRowCount">要插入到excel的数据源的总记录行数</param>
        /// <returns>要插入到当前sheet的数据</returns>
        private static DataTable GetSheetTable(DataTable dt, int intSheetLoge, int intSheetCount, int intRowMax, int intDataRowCount)
        {
            int intStartRow = intSheetLoge * intRowMax;		//记录起始行索引
            int intEndRow = (intSheetLoge + 1) * intRowMax;			//记录结束行索引		
            //若是最后一个WorkSheet，那么记录结束行索引为源DataTable行数
            if (intSheetLoge == intSheetCount - 1)
            {
                intEndRow = intDataRowCount;
            }
            DataTable dtSheet = dt.Clone();
            for (int j = intStartRow; j < intEndRow; j++)
            {
                dtSheet.Rows.Add(dt.Rows[j].ItemArray);
            }
            return dtSheet;
        }

        /// <summary>
        /// 将datatable数据导成EXCEL格式
        /// </summary>
        /// <param name="dt">数据</param>
        /// <param name="strTitle">表头信息（如果多行，请用~隔开）</param>
        /// <param name="fileFullPath">文件保存路经</param>
        public static void ExportExcel(System.Data.DataTable dt, string strTitle, string fileFullPath)
        {
            WriteExcel(dt, strTitle, fileFullPath, false);
        }

        /// <summary>
        /// 将datatable数据导成EXCEL格式
        /// </summary>
        /// <param name="isAutoFit">是否自适应excel</param>
        /// <param name="dt">数据</param>
        /// <param name="strTitle">表头信息（如果多行，请用~隔开）</param>
        /// <param name="fileFullPath">文件保存路经</param>
        public static void ExportExcel(bool isAutoFit, System.Data.DataTable dt, string strTitle, string fileFullPath)
        {
            WriteExcel(dt, strTitle, fileFullPath, isAutoFit);
        }

        /// <summary>
        /// 将datatable生成EXCEL格式
        /// </summary>
        /// <param name="dt">数据</param>
        /// <param name="strTitle">表头信息（如果多行，请用~隔开）</param>
        /// <param name="fileFullPath">文件保存路经</param>
        /// <param name="isAutoFit">是否自适应excel</param>
        private static void WriteExcel(System.Data.DataTable dt, string strTitle, string fileFullPath, bool isAutoFit)
        {
            Workbook workbook = new Workbook();
            int rows = dt.Rows.Count;
            int rowCount = dt.Rows.Count;
            int sheetCount = 1;	//WorkSheet数量
            sheetCount = GetSheetCount(rowCount, rows);

            for (int i = 0; i < sheetCount; i++)
            {
                //获得要插入到当前sheet的数据源。
                DataTable pdt = GetSheetTable(dt, i, sheetCount, rows, rowCount);
                //新增工作表
                workbook.Worksheets.Add();
                //获取当前工作表
                Worksheet worksheet = workbook.Worksheets[i];
                //  输出表头信息 **********************************************************************************	
                worksheet.Name = Convert.ToString(i + 1);
                for (int k = 0; k < pdt.Columns.Count; k++)
                {
                    //根据导入数据控制导出数据的格式
                    if (pdt.Columns[k].DataType == typeof(string))
                    {
                        worksheet.Cells.Columns[k].Style.Custom = "@";
                    }
                    else if (pdt.Columns[k].DataType == typeof(DateTime))
                    {
                        var style = new Style { Custom = "yyyy-MM-dd" };
                        worksheet.Cells[1, k].SetStyle(style);
                    }
                    else if (pdt.Columns[k].DataType == typeof(decimal))
                    {
                        //worksheet.Cells.Columns[k].Style.Custom = "General";
                        var style = new Style { Custom = "####.#####", ShrinkToFit = true };
                        worksheet.Cells[1, k].SetStyle(style);
                    }
                    worksheet.Cells[0, k].PutValue(pdt.Columns[k].Caption.Trim());
                }

                worksheet.Cells.ImportDataTable(pdt, false, 1, 0, rows, pdt.Columns.Count);
                if (isAutoFit)
                {
                    worksheet.AutoFitColumns();
                    worksheet.AutoFitRows(true);
                }
                if (!string.IsNullOrWhiteSpace(strTitle))
                {
                    string[] arrTitle = strTitle.Split('~');

                    //定义表格的标题
                    worksheet.Cells.InsertRow(0);
                    Range range = worksheet.Cells.CreateRange(worksheet.Cells[0, 0].Name, worksheet.Cells[0, pdt.Columns.Count - 1].Name);
                    range.RowHeight = 20;
                    range.Name = "ExcelTitle";
                    range.Merge();
                    Style style = workbook.Styles[workbook.Styles.Add()];
                    style.Font.Name = "宋体";
                    style.Font.IsBold = true;
                    style.Font.Size = 14;
                    style.HorizontalAlignment = TextAlignmentType.Center;
                    range.Style = style;
                    range[0, 0].PutValue(arrTitle[0]);


                    //输入次标题
                    for (int k = 1; k < arrTitle.Length; k++)
                    {
                        worksheet.Cells.InsertRow(k);
                        Range Range_Title = worksheet.Cells.CreateRange(worksheet.Cells[k, 0].Name, worksheet.Cells[k, pdt.Columns.Count - 1].Name);
                        Range_Title.RowHeight = 16;
                        Range_Title.Name = "ExcelTitle";
                        Range_Title.Merge();
                        Style style1 = workbook.Styles[workbook.Styles.Add()];
                        style1.Font.Name = "宋体";
                        style1.Font.IsBold = true;
                        style1.Font.Size = 10;
                        style1.HorizontalAlignment = TextAlignmentType.Center;
                        Range_Title.Style = style1;
                        Range_Title[0, 0].PutValue(arrTitle[k]);
                    }
                }
            }

            if (fileFullPath.ToLower().EndsWith(".xlsx"))
                workbook.Save(fileFullPath, FileFormatType.Excel2007Xlsx);
            else
                workbook.Save(fileFullPath, FileFormatType.Default);
        }

        public static void ExportExcel(DbDataReader dataReader, string strTitle, string fileFullPath, bool isAutoFit)
        {
            Workbook workbook = new Workbook();
            workbook.Worksheets.Add();
            object[] header = new object[dataReader.FieldCount];
            bool first = true;
            int rowIndex = 0;
            Style style = new Style();
            while (dataReader.Read())
            {
                for (int i = 0; i < dataReader.FieldCount; i++)
                {
                    if (first)
                    {
                        style.Custom = "@";
                        style.Font.IsBold = true;
                        workbook.Worksheets[0].Cells[rowIndex, i].SetStyle(style);
                        workbook.Worksheets[0].Cells[rowIndex, i].Value = dataReader.GetName(i);
                    }

                    style.Font.IsBold = false; if (dataReader[i] is string)
                    {
                        style.Custom = "@";
                    }
                    else if (dataReader[i] is DateTime)
                    {
                        style.Custom = "yyyy-MM-dd";
                    }
                    else if (dataReader[i] is decimal)
                    {
                        style.Custom = "####.#####";
                        style.ShrinkToFit = true;
                    }
                    workbook.Worksheets[0].Cells[rowIndex + 1, i].SetStyle(style);
                    workbook.Worksheets[0].Cells[rowIndex + 1, i].Value = dataReader[i];
                }
                first = false;
                rowIndex++;
            }

            if (isAutoFit)
            {
                workbook.Worksheets[0].AutoFitColumns();
                workbook.Worksheets[0].AutoFitRows(true);
            }
            if (fileFullPath.ToLower().EndsWith(".xlsx"))
                workbook.Save(fileFullPath, FileFormatType.Excel2007Xlsx);
            else
                workbook.Save(fileFullPath, FileFormatType.Default);
        }

        /// <summary>
        /// 将datatable数据导成EXCEL格式
        /// </summary>
        /// <param name="dt">数据</param>
        /// <param name="strTitle">表头信息（如果多行，请用~隔开）</param>
        /// <param name="fileFullPath">文件保存路经</param>
        /// <param name="hasHeader">是否包含表头</param>
        public static void ExportExcel(System.Data.DataTable dt, string strTitle, string fileFullPath, bool hasHeader)
        {
            WriteExcel(dt, strTitle, fileFullPath, hasHeader, false);
        }


        /// <summary>
        /// 将datatable数据导成EXCEL格式
        /// </summary>
        /// <param name="isAutoFit">是否自定义导出内容</param>
        /// <param name="dt">数据</param>
        /// <param name="strTitle">表头信息（如果多行，请用~隔开）</param>
        /// <param name="fileFullPath">文件保存路经</param>
        /// <param name="hasHeader">是否包含表头</param>
        public static void ExportExcel(bool isAutoFit, System.Data.DataTable dt, string strTitle, string fileFullPath, bool hasHeader)
        {
            WriteExcel(dt, strTitle, fileFullPath, hasHeader, isAutoFit);
        }

        private static void WriteExcel(System.Data.DataTable dt, string strTitle, string fileFullPath, bool hasHeader, bool isAutoFit)
        {
            Workbook workbook = new Workbook();
            int rows = dt.Rows.Count;
            int rowCount = dt.Rows.Count;
            int sheetCount = 1;	//WorkSheet数量
            sheetCount = GetSheetCount(rowCount, rows);

            for (int i = 0; i < sheetCount; i++)
            {
                //获得要插入到当前sheet的数据源。
                DataTable pdt = GetSheetTable(dt, i, sheetCount, rows, rowCount);
                //新增工作表
                workbook.Worksheets.Add();
                //获取当前工作表
                Worksheet worksheet = workbook.Worksheets[i];
                //  输出表头信息 **********************************************************************************	
                worksheet.Name = Convert.ToString(i + 1);

                //如果含有表头，则填写表头列
                if (hasHeader)
                {
                    for (int k = 0; k < pdt.Columns.Count; k++)
                    {
                        //根据导入数据控制导出数据的格式
                        if (pdt.Columns[k].DataType == typeof(string))
                        {
                            worksheet.Cells.Columns[k].Style.Custom = "@";
                        }
                        else if (pdt.Columns[k].DataType == typeof(DateTime))
                        {
                            var style = new Style { Custom = "yyyy-MM-dd" };
                            worksheet.Cells[1, k].SetStyle(style);
                        }
                        else if (pdt.Columns[k].DataType == typeof(decimal))
                        {
                            //worksheet.Cells.Columns[k].Style.Custom = "General";
                            var style = new Style { Custom = "####.######", ShrinkToFit = true };

                            worksheet.Cells[1, k].SetStyle(style);
                        }
                        worksheet.Cells[0, k].PutValue(pdt.Columns[k].Caption.Trim());
                    }
                    //如果从第二行开始填写
                    worksheet.Cells.ImportDataTable(pdt, false, 1, 0, rows, pdt.Columns.Count);
                }
                else
                {
                    for (int k = 0; k < pdt.Columns.Count; k++)
                    {
                        //根据导入数据控制导出数据的格式
                        if (pdt.Columns[k].DataType == typeof(string))
                        {
                            worksheet.Cells.Columns[k].Style.Custom = "@";
                        }
                        else if (pdt.Columns[k].DataType == typeof(DateTime))
                        {
                            var style = new Style { Custom = "yyyy-MM-dd" };
                            worksheet.Cells[0, k].SetStyle(style);
                        }
                        else if (pdt.Columns[k].DataType == typeof(decimal))
                        {
                            //worksheet.Cells.Columns[k].Style.Custom = "General";
                            var style = new Style { Custom = "####.######", ShrinkToFit = true };

                            worksheet.Cells[1, k].SetStyle(style);
                        }
                    }
                    //如果没有表头，第一行则为数据
                    worksheet.Cells.ImportDataTable(pdt, false, 0, 0, rows, pdt.Columns.Count);
                }
                if (isAutoFit)
                {
                    worksheet.AutoFitColumns();
                    worksheet.AutoFitRows(true);
                }
                if (!string.IsNullOrWhiteSpace(strTitle))
                {
                    string[] arrTitle = strTitle.Split('~');

                    //定义表格的标题
                    worksheet.Cells.InsertRow(0);
                    Range range = worksheet.Cells.CreateRange(worksheet.Cells[0, 0].Name, worksheet.Cells[0, pdt.Columns.Count - 1].Name);
                    range.RowHeight = 20;
                    range.Name = "ExcelTitle";
                    range.Merge();
                    Style style = workbook.Styles[workbook.Styles.Add()];
                    style.Font.Name = "宋体";
                    style.Font.IsBold = true;
                    style.Font.Size = 14;
                    style.HorizontalAlignment = TextAlignmentType.Center;
                    range.Style = style;
                    range[0, 0].PutValue(arrTitle[0]);


                    //输入次标题
                    for (int k = 1; k < arrTitle.Length; k++)
                    {
                        worksheet.Cells.InsertRow(k);
                        Range Range_Title = worksheet.Cells.CreateRange(worksheet.Cells[k, 0].Name, worksheet.Cells[k, pdt.Columns.Count - 1].Name);
                        Range_Title.RowHeight = 16;
                        Range_Title.Name = "ExcelTitle";
                        Range_Title.Merge();
                        Style style1 = workbook.Styles[workbook.Styles.Add()];
                        style1.Font.Name = "宋体";
                        style1.Font.IsBold = true;
                        style1.Font.Size = 10;
                        style1.HorizontalAlignment = TextAlignmentType.Center;
                        Range_Title.Style = style1;
                        Range_Title[0, 0].PutValue(arrTitle[k]);
                    }
                }
            }

            if (fileFullPath.ToLower().EndsWith(".xlsx"))
                workbook.Save(fileFullPath, FileFormatType.Excel2007Xlsx);
            else
                workbook.Save(fileFullPath, FileFormatType.Default);
        }

        /// <summary>
        /// 根据模板将数据导成EXCEL格式
        /// </summary>
        /// <param name="dt">数据集</param>
        /// <param name="strFilePath">模板文件路径</param>
        /// <param name="startRow">起始行</param>
        /// <param name="fileFullPath">全路径</param>
        public static void ExportExcelByTemplate(System.Data.DataTable dt, string strFilePath, int startRow, string fileFullPath)
        {
            ExportExcelByTemplate(null, dt, strFilePath, startRow, fileFullPath);
        }

        /// <summary>
        /// 根据模板生成带表头的Excle但是不直接打开 
        /// </summary>
        /// <param name="dtHead">表头数据</param>
        /// <param name="dt">表体数据</param>
        /// <param name="strFilePath">模板文件路径</param>
        /// <param name="startRow">起始行</param>
        /// <param name="fileFullPath">文件全路径</param>
        public static void ExportExcelByTemplate(System.Data.DataTable dtHead, System.Data.DataTable dt, string strFilePath, int startRow, string fileFullPath)
        {
            Workbook excelTemplate = new Workbook();
            Workbook excel = new Workbook();

            if (dtHead != null)
            { excel.Open(strFilePath); }
            else
            {
                //建立excel并打开模板文件
                excelTemplate.Open(strFilePath);
            }
            int intSheetCount = GetSheetCount(dt.Rows.Count, IntPageSize);
            for (int i = 0; i < intSheetCount; i++)
            {
                excel.Worksheets[i].Copy(excelTemplate.Worksheets[0]);
                //获得要插入到当前sheet的数据源。
                DataTable pdt = GetSheetTable(dt, i, intSheetCount, IntPageSize, dt.Rows.Count);
                //实例cells
                Cells cells = excel.Worksheets[i].Cells;// sheet.Cells;
                if (dtHead != null)
                {
                    cells.ImportDataTable(dtHead, false, 0, 0, false);
                }
                cells.ImportDataTable(pdt, false, startRow, 0, true);
                if (i < intSheetCount - 1)
                {
                    excel.Worksheets.Add();
                }
            }
            excel.Save(fileFullPath, SaveFormat.Auto);
        }

        /// <summary>
        /// 根据模板将数据导成过滤过指定列的EXCEL格式
        /// </summary>
        /// <param name="dt">数据集</param>
        /// <param name="strFilePath">模板文件路径</param>
        /// <param name="intStartRow">起始行</param>
        /// <param name="strFileFullPath">全路径</param>
        /// <param name="arrStrFilterColumn">过滤指定列</param>
        public static void ExportExcelByTemplate(System.Data.DataTable dt, string strFilePath, int intStartRow, string strFileFullPath, string[] arrStrFilterColumn)
        {
            ExportExcelByTemplate_Handler(dt, strFilePath, intStartRow, strFileFullPath, arrStrFilterColumn, "0");
        }

        /// <summary>
        /// 根据模板将数据导成包含指定列的EXCEL格式
        /// </summary>
        /// <param name="dt">数据集</param>
        /// <param name="strFilePath">模板文件路径</param>
        /// <param name="intStartRow">起始行</param>
        /// <param name="strFileFullPath">全路径</param>
        /// <param name="arrStrVisibleColumn">包含指定的列 </param>
        public static void ExportExcelByTemplateByVisible(System.Data.DataTable dt, string strFilePath, int intStartRow, string strFileFullPath, string[] arrStrVisibleColumn)
        {
            ExportExcelByTemplate_Handler(dt, strFilePath, intStartRow, strFileFullPath, arrStrVisibleColumn, "1");
        }

        /// <summary>
        /// 根据模板将数据导成可配置包含或排除指定类的EXCEL格式
        /// </summary>
        /// <param name="dt">数据集</param>
        /// <param name="strFilePath">模板文件路径</param>
        /// <param name="intStartRow">起始行</param>
        /// <param name="strFileFullPath">全路径</param>
        /// <param name="arrVisibleColumn">只包含这些列、或除去这些列</param>
        /// <param name="strFlag">1表示过滤:取只存在于arrVisibleColumn中列，0表示包含:取不存在于arrVisibleColumn中列</param>
        private static void ExportExcelByTemplate_Handler(System.Data.DataTable dt, string strFilePath, int intStartRow, string strFileFullPath, string[] arrVisibleColumn, string strFlag)
        {
            Workbook excelTemplate = new Workbook();
            Workbook excel = new Workbook();
            int intSheetCount = GetSheetCount(dt.Rows.Count, IntPageSize);
            //建立excel并打开模板文件
            excelTemplate.Open(strFilePath);

            for (int i = 0; i < intSheetCount; i++)
            {
                excel.Worksheets[i].Copy(excelTemplate.Worksheets[0]);
                int intDataStartRow = i * IntPageSize; //记录起始行索引
                int intEndRow = (i + 1) * IntPageSize; //记录结束行索引		
                //若是最后一个WorkSheet，那么记录结束行索引为源DataTable行数
                if (i == intSheetCount - 1)
                {
                    intEndRow = dt.Rows.Count;
                }
                DataTable dtSheet = new DataTable(); //分页数据
                var arrListStringVisible = new List<string>();
                foreach (DataColumn dc in dt.Columns)
                {
                    dtSheet.Columns.Add(dc.ColumnName, dc.DataType);
                    arrListStringVisible.Add(dc.ColumnName);
                }
                for (int j = intDataStartRow; j < intEndRow; j++)
                {
                    dtSheet.Rows.Add(dt.Rows[j].ItemArray);
                }
                //1代表包含
                if (strFlag == "1")
                {
                    //过滤指定列
                    foreach (string strColumn in arrVisibleColumn)
                    {
                        if (Array.Exists(arrListStringVisible.ToArray(), a =>
                            {
                                return a.Equals(strColumn,
                                         StringComparison.
                                             OrdinalIgnoreCase);
                            }))
                        {
                            arrListStringVisible.Remove(strColumn.ToUpper());
                        }
                    }
                    if (arrListStringVisible.Count > 0)
                    {
                        for (int m = 0; m < arrListStringVisible.Count; m++)
                        {
                            dtSheet.Columns.Remove(arrListStringVisible[m].ToString());
                        }
                    }
                }
                else
                {
                    //过滤指定列
                    if (arrVisibleColumn.Length > 0)
                    {
                        foreach (string strColumn in arrVisibleColumn)
                        {
                            dtSheet.Columns.Remove(strColumn);
                        }
                    }
                }
                //实例cells
                Cells cells = excel.Worksheets[i].Cells; // sheet.Cells;
                cells.ImportDataTable(dtSheet, false, intStartRow, 0, true);
                if (i < intSheetCount - 1)
                {
                    excel.Worksheets.Add();
                }
            }
            excel.Save(strFileFullPath, SaveFormat.Auto);
        }

        /// <summary>
        /// 根据模板将datareader数据导成EXCEL格式
        /// </summary>
        /// <param name="dataReader">数据集</param>
        /// <param name="strFilePath">模板文件路径</param>
        /// <param name="intStartRow">起始行</param>
        /// <param name="strFileFullPath">全路径</param>
        public static void ExportExcelByTemplate(DbDataReader dataReader, string strFilePath, int intStartRow, string strFileFullPath)
        {
            ExportExcelByTemplate(dataReader, strFilePath, intStartRow, strFileFullPath, null);
        }

        /// <summary>
        /// 根据模板将datareaser数据导成过滤指定列的EXCEL格式
        /// </summary>
        /// <param name="dataReader">数据集</param>
        /// <param name="strFilePath">模板文件路径</param>
        /// <param name="intStartRow">起始行</param>
        /// <param name="strFileFullPath">全路径</param>
        /// <param name="arrFilterColumn">过滤指定列，列名（别名）列表</param>
        public static void ExportExcelByTemplate(DbDataReader dataReader, string strFilePath, int intStartRow, string strFileFullPath, string[] arrFilterColumn)
        {
            Workbook excelTemplate = new Workbook();
            Workbook excel = new Workbook();

            //建立excel并打开模板文件
            excelTemplate.Open(strFilePath);
            int intSheetRowCount = 0;
            int intSheetCount = 0;

            excel.Worksheets[intSheetRowCount].Copy(excelTemplate.Worksheets[0]);
            Cells cells = excel.Worksheets[intSheetCount].Cells;// sheet.Cells;
            Object[] arrObj = new Object[dataReader.FieldCount];//
            List<string> list = null;
            if (arrFilterColumn != null)
            {
                list = new List<string>(arrFilterColumn);//过滤指定列，列名（别名）列表
            }
            while (dataReader.Read())
            {
                if (intSheetRowCount == IntPageSize)
                {
                    excel.Worksheets.Add();
                    intSheetCount += 1;
                    excel.Worksheets[intSheetCount].Copy(excelTemplate.Worksheets[0]);
                    intSheetRowCount = 0;
                    cells = excel.Worksheets[intSheetCount].Cells;
                }
                if (arrFilterColumn != null)
                {
                    ArrayList arrList = new ArrayList();
                    for (int i = 0; i < dataReader.FieldCount; i++)
                    {
                        if (!Array.Exists(list.ToArray(), p =>
                                                             {
                                                                 return p.Equals(dataReader.GetName(i),
                                                                                 StringComparison.OrdinalIgnoreCase);
                                                             }))
                        {
                            arrList.Add(dataReader.GetValue((i)));
                        }
                    }
                    Object[] arrObjTmp = arrList.ToArray();
                    cells.ImportObjectArray(arrObjTmp, intStartRow + intSheetRowCount, 0, false);
                }
                else
                {
                    dataReader.GetValues(arrObj);
                    cells.ImportObjectArray(arrObj, intStartRow + intSheetRowCount, 0, false);
                }
                intSheetRowCount += 1;
            }
            excel.Worksheets.Add();
            excel.Save(strFileFullPath, SaveFormat.Auto);
        }
        #endregion

        #region 读取Excel到DS或DT
        /// <summary>
        /// 读取Excel到dataset
        /// </summary>
        /// <param name="path">路径</param>
        /// <param name="strStart">起始列</param>
        /// <param name="strEnd">结束列</param>
        /// <returns>返回值dataset</returns>
        public static DataSet ReadExcelToDataSet(string path, string strStart, string strEnd)
        {
            string strConn = "Provider=Microsoft.Jet.Oledb.4.0;Data Source=" + path + ";Extended Properties=" + "\"" + "Excel 8.0;HDR=NO;IMEX=1;" + "\"";
            OleDbDataAdapter myCommand = null;
            DataSet ds = null;
            OleDbConnection conn = new OleDbConnection(strConn);
            conn.Open();
            DataTable schemaTable = conn.GetOleDbSchemaTable(OleDbSchemaGuid.Tables, null);
            string tableName = schemaTable.Rows[0][2].ToString().Trim();
            if (strStart != "") strStart = strStart + ",";
            if (strEnd != "") strEnd = "," + strEnd;
            string strExcel = "select " + strStart + "a.*" + strEnd
                + " from  [" + tableName + "] a";

            myCommand = new OleDbDataAdapter(strExcel, strConn);
            ds = new DataSet();
            myCommand.Fill(ds);

            conn.Close();
            return ds;
        }

        #region 读取Excel到DT
        /// <summary>
        /// 读取Excel，并返回Excel第一个Sheet中的记录数
        /// </summary>
        /// <param name="path">路径</param>
        /// <returns>返回记录数</returns>
        public static int ReadExcelReturnTotalRows(string path)
        {
            Workbook workbook = new Workbook();

            if (path.ToLower().EndsWith(".xlsx"))
                workbook.Open((System.IO.Stream)File.OpenRead(path), FileFormatType.Excel2007Xlsx);
            else
                workbook.Open(path);

            //定义工作表
            Worksheet worksheet = workbook.Worksheets[0];
            return worksheet.Cells.Rows.Count;
        }

        /// <summary>
        /// 读取Excel到object[,]，并按指定行开始，读取指定记录数
        /// </summary>
        /// <param name="path">路径</param>
        /// <param name="beginRows">开始行数</param>
        /// <param name="totalRows">读取总行数</param>
        /// <returns>返回值DataTable</returns>
        public static DataTable ReadExcelToDataTable(string path, int beginRows, int totalRows)
        {
            return ReadExcelToDataTable_Handler(path, beginRows, totalRows);
        }
        /// <summary>
        /// 读取Excel到DataTable，并按指定行开始，读取指定记录数
        /// </summary>
        /// <param name="path">路径</param>
        /// <param name="beginRows">开始行数</param>
        /// <param name="totalRows">读取总行数</param>
        /// <returns>返回值DataTable</returns>
        public static object[,] ReadExcelToArray(string path, int beginRows, int totalRows)
        {
            return ReadExcelToArray_Handler(path, beginRows, totalRows);
        }
        /// <summary>
        /// 读取Excel到DataTable
        /// </summary>
        /// <param name="path">路径</param>
        /// <returns>返回datatable</returns>
        public static DataTable ReadExcelToDataTable(string path)
        {
            return ReadExcelToDataTable_Handler(path);
        }

        /// <summary>
        /// 读取Excel到DataTable
        /// </summary>
        /// <param name="path">路径</param>
        /// <param name="strStart">起始列</param>
        /// <param name="strEnd">结束列</param>
        /// <returns>返回值DataTable</returns>
        public static DataTable ReadExcelToDataTable(string path, string strStart, string strEnd)
        {
            return ReadExcelToDataTable_Handler(path, strStart, null, strEnd, 2);
        }

        /// <summary>
        /// 读取Excel到DataTable
        /// </summary>
        /// <param name="strPath">路径</param>
        /// <param name="strStart">起始列</param>
        /// <param name="strSecondCol">第二列</param>
        /// <param name="strEnd">结束列</param>
        /// <returns>返回值DataTable</returns>
        public static DataTable ReadExcelToDataTable(string strPath, string strStart, string strSecondCol, string strEnd)
        {
            return ReadExcelToDataTable_Handler(strPath, strStart, strSecondCol, strEnd, 3);
        }
        /// <summary>
        /// 读取Excel到DataTable，并按指定行开始，读取指定的记录数
        /// </summary>
        /// <param name="strPath">路径</param>
        /// <param name="beginRows">开始行数</param>
        /// <param name="totalRows">总记录数</param>
        /// <returns>返回datatable</returns>
        private static DataTable ReadExcelToDataTable_Handler(string strPath, int beginRows, int totalRows)
        {
            Workbook workbook = new Workbook();

            if (strPath.ToLower().EndsWith(".xlsx"))
                workbook.Open((System.IO.Stream)File.OpenRead(strPath), FileFormatType.Excel2007Xlsx);
            else
                workbook.Open(strPath);

            //定义工作表
            Worksheet worksheet = workbook.Worksheets[0];
            //读取第一行作为列名
            var columnSources = worksheet.Cells.ExportDataTable(0, 0, 1, worksheet.Cells.MaxColumn + 1);
            if (columnSources.Rows.Count == 0 || columnSources.Columns.Count == 0) return new DataTable();
            var dtSource = new DataTable();
            for (var i = 0; i < columnSources.Columns.Count; i++)
            {
                dtSource.Columns.Add(columnSources.Rows[0][i].ToString());
            }
            if (beginRows > worksheet.Cells.MaxRow + 1)
            {
                return new DataTable();
            }

            //int totalRow;
            //if (totalRows > worksheet.Cells.MaxRow + 1)
            //    totalRow = beginRows + totalRows - worksheet.Cells.MaxRow + 1;
            //else
            //    totalRow = totalRows;

            worksheet.Cells.ExportDataTable(dtSource, beginRows, 0, totalRows, false);

            return dtSource;
        }

        /// <summary>
        /// 读取Excel到DataTable，并按指定行开始，读取指定的记录数
        /// </summary>
        /// <param name="strPath">路径</param>
        /// <param name="beginRows">开始行数</param>
        /// <param name="totalRows">总记录数</param>
        /// <returns>返回查询结果集</returns>
        private static object[,] ReadExcelToArray_Handler(string strPath, int beginRows, int totalRows)
        {
            Workbook workbook = new Workbook();

            if (strPath.ToLower().EndsWith(".xlsx"))
                workbook.Open((System.IO.Stream)File.OpenRead(strPath), FileFormatType.Excel2007Xlsx);
            else
                workbook.Open(strPath);

            //定义工作表
            Worksheet worksheet = workbook.Worksheets[0];
            ////读取第一行作为列名
            //var columnSources = worksheet.Cells.ExportArray(0, 0, 1, worksheet.Cells.Columns.Count);
            //if (columnSources.Length==0) return null;
            //var dtSource = new DataTable();
            //for (var i = 0; i < columnSources.Columns.Count; i++)
            //{
            //    dtSource.Columns.Add(columnSources.Rows[0][i].ToString());
            //}
            //if (beginRows > worksheet.Cells.MaxRow + 1)
            //{
            //    return new DataTable();
            //}

            //int totalRow;
            //if (totalRows > worksheet.Cells.MaxRow + 1)
            //    totalRow = beginRows + totalRows - worksheet.Cells.MaxRow + 1;
            //else
            //    totalRow = totalRows;

            var objArray = worksheet.Cells.ExportArray(beginRows, 0, totalRows, worksheet.Cells.MaxColumn + 1);
            return objArray;
        }
        /// <summary>
        /// 读取Excel到DataTable
        /// </summary>
        /// <param name="strPath">路径</param>
        /// <returns>返回datatable</returns>
        private static DataTable ReadExcelToDataTable_Handler(string strPath)
        {
            Workbook workbook = new Workbook();

            if (strPath.ToLower().EndsWith(".xlsx"))
                workbook.Open((System.IO.Stream)File.OpenRead(strPath), FileFormatType.Excel2007Xlsx);
            else
                workbook.Open(strPath);

            //定义工作表
            Worksheet worksheet = workbook.Worksheets[0];

            var dataTable = worksheet.Cells.ExportDataTable(0, 0, worksheet.Cells.MaxRow + 1, worksheet.Cells.MaxColumn + 1);
            return dataTable;
        }
        /// <summary>
        /// 读取Excel到DataTable,并在开头第一列，第二列和最后一列插入固定值
        /// </summary>
        /// <param name="strPath">路径</param>
        /// <param name="strStart">起始列</param>
        /// <param name="strSecondCol">第二列</param>
        /// <param name="strEnd">结束列</param>
        /// <param name="intStartColumns">一共多加了几列</param>
        /// <returns>返回值</returns>
        private static DataTable ReadExcelToDataTable_Handler(string strPath, string strStart, string strSecondCol, string strEnd, int intStartColumns)
        {
            Workbook workbook = new Workbook();

            if (strPath.ToLower().EndsWith(".xlsx"))
                workbook.Open((System.IO.Stream)File.OpenRead(strPath), FileFormatType.Excel2007Xlsx);
            else
                workbook.Open(strPath);


            //定义工作表
            Worksheet worksheet = workbook.Worksheets[0];
            DataTable dataTable = new DataTable();
            dataTable = worksheet.Cells.ExportDataTable(0, 0, worksheet.Cells.MaxRow + 1, worksheet.Cells.MaxColumn + 1);

            //判断是否需要加入前置列或后置列
            int intLen = dataTable.Columns.Count;
            if (strStart != "") intLen++;
            if (strEnd != "") intLen++;
            if (strSecondCol != "" && intStartColumns == 3) intLen++;

            //没有前置列和后置列，直接返回
            if (intLen == dataTable.Columns.Count)
                return dataTable;

            //有前置列或后置列，向新构造的DataTable写数据
            DataTable dtReturn = new DataTable();
            for (int i = 0; i < intLen; i++)
            {
                dtReturn.Columns.Add("col" + i.ToString());
            }

            object[] obj = new object[intLen];
            for (int i = 0; i < dataTable.Rows.Count; i++)
            {
                if (intLen == dataTable.Columns.Count + intStartColumns)
                {
                    obj[0] = strStart;
                    if (intStartColumns == 3)
                    {
                        obj[1] = strSecondCol;
                    }
                    for (int j = 0; j < dataTable.Columns.Count; j++)
                    {
                        obj[j + (intStartColumns - 1)] = dataTable.Rows[i][j];
                    }
                    obj[intLen - 1] = strEnd;
                }
                else if (strStart == "")
                {
                    if (intStartColumns == 3) { obj[0] = strSecondCol; }
                    for (int j = 0; j < dataTable.Columns.Count; j++)
                    {
                        obj[j + 1] = dataTable.Rows[i][j];
                    }
                    obj[intLen - 1] = strEnd;
                }
                else if (strSecondCol == "" && intStartColumns == 3)
                {
                    obj[0] = strStart;
                    for (int j = 0; j < dataTable.Columns.Count; j++)
                    {
                        obj[j] = dataTable.Rows[i][j];
                    }
                    obj[intLen - 1] = strEnd;
                }
                else if (strEnd == "")
                {
                    obj[0] = strStart;
                    if (intStartColumns == 3)
                    {
                        obj[1] = strSecondCol;
                        for (int j = 0; j < dataTable.Columns.Count; j++)
                        {
                            obj[j + 2] = dataTable.Rows[i][j];
                        }
                    }
                    else
                    {
                        for (int j = 0; j < dataTable.Columns.Count; j++)
                        {
                            obj[j] = dataTable.Rows[i][j];
                        }
                    }
                }
                dtReturn.Rows.Add(obj);
            }
            return dtReturn;
        }


        /// <summary>
        /// 读取Excel到DataTable,并前面插入传入的列数，最后插入一列
        /// </summary>
        /// <param name="path">路径</param>
        /// <param name="arrCol">需要插入到最前面对列</param>
        /// <param name="strEnd">结束列</param>
        /// <returns>返回值DataTable</returns>
        public static DataTable ReadExcelToDataTable(string path, string[] arrCol, string strEnd)
        {
            Workbook workbook = new Workbook();

            if (path.ToLower().EndsWith(".xlsx"))
                workbook.Open((System.IO.Stream)File.OpenRead(path), FileFormatType.Excel2007Xlsx);
            else
                workbook.Open(path);

            //定义工作表
            Worksheet worksheet = workbook.Worksheets[0];
            DataTable dataTable = new DataTable();
            dataTable = worksheet.Cells.ExportDataTableAsString(0, 0, worksheet.Cells.MaxRow + 1, worksheet.Cells.MaxColumn + 1);

            //判断是否需要加入前置列或后置列
            int intLen = dataTable.Columns.Count;
            //记录真实的导入的列
            string[] arrTrueAdd = new string[arrCol.Length];
            //真实的导入的列数
            int intAddCol = 0;
            if (arrCol.Length > 0)
            {
                for (int col = 0; col < arrCol.Length; col++)
                {
                    if (arrCol[col].ToString().Trim() != "")
                    {
                        intLen++;
                        arrTrueAdd[intAddCol] = arrCol[col];
                        intAddCol++;
                    }
                }
            }
            if (strEnd != "") intLen++;
            //没有前置列和后置列，直接返回
            if (intLen == dataTable.Columns.Count)
                return dataTable;

            //有前置列或后置列，向新构造的DataTable写数据
            DataTable dtReturn = new DataTable();
            for (int i = 0; i < intLen; i++)
            {
                dtReturn.Columns.Add("col" + i.ToString());
            }

            object[] obj = new object[intLen];
            for (int i = 0; i < dataTable.Rows.Count; i++)
            {
                if (intLen == dataTable.Columns.Count + intAddCol + 1)
                {
                    //obj[0] = strStart;
                    //obj[1] = strSecondCol;
                    for (int k = 0; k < intAddCol; k++)
                    {
                        obj[k] = arrTrueAdd[k].ToString().Trim();
                    }
                    for (int j = 0; j < dataTable.Columns.Count; j++)
                    {
                        obj[j + intAddCol] = dataTable.Rows[i][j];
                    }
                    obj[intLen - 1] = strEnd;
                }
                else if (strEnd == "")
                {
                    for (int k = 0; k < intAddCol; k++)
                    {
                        obj[k] = arrTrueAdd[k].ToString().Trim();
                    }
                    for (int j = 0; j < dataTable.Columns.Count; j++)
                    {
                        obj[j + intAddCol] = dataTable.Rows[i][j];
                    }
                }

                dtReturn.Rows.Add(obj);
            }

            return dtReturn;
        }

        /// <summary>
        /// 读取Excel到Datatable，并在前面插入固定一列，最后插入一列，并返回是否成功过
        /// </summary>
        /// <param name="physicalPath">Excel文件绝对路径</param>
        /// <param name="columnMapping">字段映射关系(第1列为导入的列名,第2列为保存列名)</param>
        /// <param name="strStart">其他用途</param>
        /// <param name="strEnd">其他用途</param>
        /// <param name="dataTable">输出dataTable</param>
        /// <returns>success:读取成功,noDefinedColumn:未定义导入列,为否则返回具体错误</returns>
        public static string ReadExcelToDataTableByOLE(string physicalPath, DataRow[] columnMapping, string strStart, string strEnd, out DataTable dataTable)
        {
            dataTable = null;
            OleDbConnection conn = null;
            string strConn = string.Format("Provider=Microsoft.Jet.Oledb.4.0;Data Source={0};Extended Properties='Excel 8.0;HDR=Yes;IMEX=1;'", physicalPath);
            if (null == strStart || strStart.Trim() == "")
                strStart = "noValue";
            else if (null == strEnd || strEnd.Trim() == "")
                strEnd = "noValue";

            strStart = strStart + " as [" + HeadColumn + "],";
            strEnd = "," + strEnd + " as [" + FootColumn + "]";

            string field = "";
            foreach (DataRow row in columnMapping)
            {
                field = field + "," + row[0].ToString();
            }

            if (field == "")
                return "noDefinedColumn";
            else if (field.StartsWith(","))
                field = field.Substring(1);

            field = field.ToLower();

            try
            {
                OleDbDataAdapter myCommand = null;
                conn = new OleDbConnection(strConn);
                conn.Open();
                DataTable schemaTable = conn.GetOleDbSchemaTable(OleDbSchemaGuid.Tables, null);
                //取第一个sheet名
                string tableName = schemaTable.Rows[0][2].ToString().Trim();

                string strSql = string.Format("select {0}*{1} from [{2}]", strStart, strEnd, tableName);

                myCommand = new OleDbDataAdapter(strSql, conn);
                dataTable = new DataTable();
                dataTable.RowChanged += new DataRowChangeEventHandler(DataTableRowChanged);
                myCommand.Fill(dataTable);

                /*因OracleBulkCopy进行列ColumnMapping时源DataTable列必须全部映射,不充许有多余的列,SqlBulkCopy不存在该问题,只映射指定的列*/
                //去除Excel多余的列
                using (DataTable cloneDt = dataTable.Clone())
                {
                    foreach (DataColumn column in cloneDt.Columns)
                    {
                        if (field.IndexOf(column.ColumnName.ToLower()) < 0)
                        {
                            dataTable.Columns.Remove(column.ColumnName);
                        }
                    }
                }

                //检查Excel列名是否符合格式
                string[] fild = field.Split(',');
                if (dataTable.Rows.Count > 0)
                {
                    for (int i = 0; i < fild.Length; i++)
                    {
                        try
                        {
                            object obj = dataTable.Rows[0][fild[i]];
                        }
                        catch
                        {
                            return string.Format("导入的Excel文件不符合格式，未找到列：{0}", fild[i]);
                        }
                    }
                }
                return "success";
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
            finally
            {
                conn.Close();
            }
        }
        #endregion

        #endregion
    }
}
