package com.app.dao.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportFileExcell {
    public ImportBean getListImport(String fileName, int startDataRow, int columnConfig) throws Exception {
        ImportBean importBean = new ImportBean();
        List<Object[]> dataList = new ArrayList<>();
        List<Integer> rows = new ArrayList<>();
        List<String[]> excellDatas = ReadExelFile.readXLSFile(new File(fileName), 100, startDataRow);
        for (int row = startDataRow; row < excellDatas.size(); row++) {
            String[] cells = excellDatas.get(row);
            if (cells.length >= 1) {
                boolean emptyRow = true;
                for (int col = 1; col < columnConfig; col++) {
                    if (col < cells.length) {
                        String content = cells[col];
                        if (!CommonUtils.isNullOrEmpty(content)) {
                            emptyRow = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if(!emptyRow) {
                    dataList.add(cells);
                    rows.add(row);
                }
            }
        }
        importBean.setDataList(dataList);
        importBean.setRows(rows);
        return importBean;
    }

    public static class ImportBean {
        List<Object[]> dataList;
        List<Integer> rows;

        public List<Object[]> getDataList() {
            return dataList;
        }

        public void setDataList(List<Object[]> dataList) {
            this.dataList = dataList;
        }

        public List<Integer> getRows() {
            return rows;
        }

        public void setRows(List<Integer> rows) {
            this.rows = rows;
        }
    }


    /**
     * Doi tuong loi khi thuc hien nghiep vu Import Excel.
     *
     * @author Biennv1
     * @since 1.0
     * @version 1.0
     */
    public static class ImportErrorBean {

        /**
         * Dong
         */
        private int row;
        /**
         * Cot
         */
        private int column;
        /**
         * Nhan cua cot
         */
        private String columnLabel;
        /**
         * Mo ta
         */
        private String description;
        /**
         * Noi dung file Excel
         */
        private String content;

        /**
         * Ham tao mac dinh.
         */
        public ImportErrorBean() {
        }

        /**
         * Ham tao.
         *
         * @param row Hang
         * @param icolumn Cot
         * @param description Mo ta
         * @param content Noi dung
         */
        public ImportErrorBean(Integer row, Integer icolumn, String description, String content) {
            final int ALPHABET_NUMBER = 26; // so chu cai
            this.row = row + 1;
            this.column = icolumn + 1;
            this.description = description;
            this.content = content;

            //this.columnLabel = Character.forDigit(Character.digit('A', 10), 10);
            if (icolumn < ALPHABET_NUMBER) {
                this.columnLabel = String.valueOf((char) ('A' + icolumn));
            } else {
                int temp = icolumn / ALPHABET_NUMBER;
                icolumn -= ALPHABET_NUMBER * temp;
                this.columnLabel = String.valueOf((char) ('A' + temp - 1)) + String.valueOf((char) ('A' + icolumn));
            }
        }

        /**
         *
         * @return
         */
        public String getContent() {
            return content;
        }

        /**
         *
         * @param content
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         *
         * @return
         */
        public String getColumnLabel() {
            return columnLabel;
        }

        /**
         *
         * @param columnLabel
         */
        public void setColumnLabel(String columnLabel) {
            this.columnLabel = columnLabel;
        }

        /**
         *
         * @return
         */
        public int getColumn() {
            return column;
        }

        /**
         *
         * @param column
         */
        public void setColumn(int column) {
            this.column = column;
        }

        /**
         *
         * @return
         */
        public String getDescription() {
            return description;
        }

        /**
         *
         * @param description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         *
         * @return
         */
        public int getRow() {
            return row;
        }

        /**
         *
         * @param row
         */
        public void setRow(int row) {
            this.row = row;
        }
    }
}
