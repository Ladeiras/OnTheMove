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
    public class Lib_Contacto
    {
        public static Contacto GetContacto(string id, string contact)
        {
            Contacto contacto = null;
            List<Decimal> telefones = new List<Decimal>();

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();

            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_contacto where nome = '"+contact+"' and idaeroporto = "+id;
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();
            
            while(dr.Read())
            {
                contacto = new Contacto();
                contacto.Id = (Decimal)dr.GetValue(0);
                contacto.IdAeroporto = (Decimal)dr.GetValue(1);
                contacto.Nome = (string)dr.GetValue(2);
                contacto.Titulo = (string)dr.GetValue(3);
                contacto.Website = (string)dr.GetValue(4);
                contacto.MapaURL = (string)dr.GetValue(5);
                contacto.Descricao = (string)dr.GetValue(6);
            }
            //conn.Dispose();

            if(contacto != null)
            {
                OracleCommand cmd2 = new OracleCommand();
                cmd2.Connection = conn;
                cmd2.CommandText = "select * from otm_contacto_telefone where idcontacto = " + contacto.Id;
                cmd2.CommandType = CommandType.Text;
                OracleDataReader dr2 = cmd2.ExecuteReader();

                while (dr2.Read())
                {
                    telefones.Add((Decimal)dr2.GetValue(2));
                }

                contacto.Telefones = telefones;
            }

            conn.Dispose();
            return contacto;
        }
    }
}