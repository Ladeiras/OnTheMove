using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Oracle.DataAccess.Client;
using Oracle.DataAccess.Types;
using System.Data;
using OnTheMove.Lib.Model;

namespace OnTheMove.Lib
{
    public class Lib_Loja
    {
        public static List<Model.Loja> ListLojas(string id)
        {
            Loja loja = null;
            List<Model.Loja> listLojas = new List<Model.Loja>();

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();
            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_loja where idaeroporto = "+id;
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();
            
            while(dr.Read())
            {
                loja = new Loja();
                loja.Id = (Decimal)dr.GetValue(0);
                loja.Categoria = (string)dr.GetValue(2);
                loja.Nome = (string)dr.GetValue(3);
                loja.ImagemUrl = (string)dr.GetValue(4);
                loja.MapaUrl = (string)dr.GetValue(5);
                loja.Webmail = (string)dr.GetValue(6);
                loja.Website = (string)dr.GetValue(7);
                loja.Telefone = (Decimal)dr.GetValue(8);
                loja.Descricao = (string)dr.GetValue(9);
                loja.ComPromocao = (Decimal)dr.GetValue(10);

                if (loja.ComPromocao == 1)
                    loja.Promocao = (string)dr.GetValue(11);
                else
                    loja.Promocao = "";

                listLojas.Add(loja);
            }
            conn.Dispose();

            return listLojas;
        }
    }
}