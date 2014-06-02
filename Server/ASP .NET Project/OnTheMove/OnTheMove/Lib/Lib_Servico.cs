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
    public class Lib_Servico
    {
        public static Servico GetServico(string id,string serv)
        {
            Servico servico = null;
            Mapa mapa = null;
            List<Mapa> mapas = new List<Mapa>();

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();

            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_servico where nome = '"+serv+"' and idaeroporto = "+id;
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();
            
            while(dr.Read())
            {
                servico = new Servico();
                servico.Id = (Decimal)dr.GetValue(0);
                servico.Nome = (string)dr.GetValue(2);
                servico.Descricao = (string)dr.GetValue(3);
            }
            //conn.Dispose();

            if (servico!=null)
            {
                OracleCommand cmd2 = new OracleCommand();
                cmd2.Connection = conn;
                cmd2.CommandText = "select * from otm_mapa where idservico = " + servico.Id;
                cmd2.CommandType = CommandType.Text;
                OracleDataReader dr2 = cmd2.ExecuteReader();

                while (dr2.Read())
                {
                    mapa = new Mapa();
                    mapa.Id = (Decimal)dr2.GetValue(0);
                    mapa.Localizacao = (string)dr2.GetValue(2);
                    mapa.Url = (string)dr2.GetValue(3);
                    mapas.Add(mapa);
                }

                servico.Mapas = mapas;
            }

            conn.Dispose();
            return servico;
        }
    }
}