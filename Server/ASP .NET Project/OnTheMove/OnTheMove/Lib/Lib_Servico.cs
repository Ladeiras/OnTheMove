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
            List<Decimal> telefones = new List<Decimal>();

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
                servico.Titulo = (string)dr.GetValue(3);

                if (dr.GetValue(4) == DBNull.Value)
                    servico.Website = "";
                else
                    servico.Website = (string)dr.GetValue(4);

                if (dr.GetValue(5) == DBNull.Value)
                    servico.Webmail = "";
                else
                    servico.Webmail = (string)dr.GetValue(5);

                if (dr.GetValue(6) == DBNull.Value)
                    servico.Descricao = "";
                else
                    servico.Descricao = (string)dr.GetValue(6);
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

                OracleCommand cmd3 = new OracleCommand();
                cmd3.Connection = conn;
                cmd3.CommandText = "select * from otm_servico_telefone where idservico = " + servico.Id;
                cmd3.CommandType = CommandType.Text;
                OracleDataReader dr3 = cmd3.ExecuteReader();

                while (dr3.Read())
                {
                    telefones.Add((Decimal)dr3.GetValue(2));
                }

                servico.Telefones = telefones;
            }

            conn.Dispose();
            return servico;
        }
    }
}