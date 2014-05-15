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
    public class Lib_Aeroporto
    {
        public static List<Model.Aeroporto> ListAeroportos()
        {
            Aeroporto ap = null;
            List<Model.Aeroporto> listAeroportos = new List<Model.Aeroporto>();

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();
            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_aeroporto";
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();
            
            while(dr.Read())
            {
                ap = new Aeroporto();
                ap.Id = (Decimal)dr.GetValue(0);
                ap.Pais = (string)dr.GetValue(1);
                ap.Cidade = (string) dr.GetValue(2);
                ap.Nome = (string) dr.GetValue(3);
                ap.Latitude = (double)dr.GetValue(4);
                ap.Longitude = (double)dr.GetValue(5);

                listAeroportos.Add(ap);
            }
            conn.Dispose();

            return listAeroportos;
        }
    }
}