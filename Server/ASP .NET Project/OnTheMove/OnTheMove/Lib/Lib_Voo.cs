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
    public class Lib_Voo
    {
        public static Voo GetVoo(string id)
        {
            Voo voo = null;

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();
            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_voo where idvoo = "+id;
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();
            
            while(dr.Read())
            {
                voo = new Voo();
                voo.Id = (Decimal)dr.GetValue(0);
                voo.CodigoVoo = (Decimal)dr.GetValue(1);
                voo.CodigoCompanhia = (string)dr.GetValue(2);
                voo.PartidaAeroportoId = (Decimal)dr.GetValue(3);
                voo.ChegadaAeroportoId = (Decimal)dr.GetValue(4);
                voo.PartidaCidade = (String)dr.GetValue(5);
                voo.ChegadaCidade = (String)dr.GetValue(6);
                voo.PartidaTempoEstimado = (DateTime)dr.GetValue(7);
                voo.ChegadaTempoEstimado = (DateTime)dr.GetValue(8);
                voo.PartidaTempoReal = (DateTime)dr.GetValue(9);
                voo.ChegadaTempoReal = (DateTime)dr.GetValue(10);

                voo.Terminal = (Decimal)dr.GetValue(11);
                voo.CheckinInicio = (DateTime)dr.GetValue(12);
                voo.CheckinFim = (DateTime)dr.GetValue(13);
                voo.PortaEmbarque = (Decimal)dr.GetValue(14);
                voo.Embarque = (DateTime)dr.GetValue(15);
                voo.TapeteBagagem = (Decimal)dr.GetValue(16);
                voo.Bagagem = (DateTime)dr.GetValue(17);
                voo.PortaDesembarque = (Decimal)dr.GetValue(18);
                voo.Desembarque = (DateTime)dr.GetValue(19);

            }
            conn.Dispose();

            return voo;
        }

        //Partidas
        public static List<Model.Voo> ListPartidas(string id)
        {
            Voo voo = null;
            List<Model.Voo> listVoos = new List<Model.Voo>();

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();
            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_voo where partidaaeroportoid = " + id;
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                voo = new Voo();
                voo.Id = (Decimal)dr.GetValue(0);
                voo.CodigoVoo = (Decimal)dr.GetValue(1);
                voo.CodigoCompanhia = (string)dr.GetValue(2);
                voo.PartidaAeroportoId = (Decimal)dr.GetValue(3);
                voo.ChegadaAeroportoId = (Decimal)dr.GetValue(4);
                voo.PartidaCidade = (String)dr.GetValue(5);
                voo.ChegadaCidade = (String)dr.GetValue(6);
                voo.PartidaTempoEstimado = (DateTime)dr.GetValue(7);
                voo.ChegadaTempoEstimado = (DateTime)dr.GetValue(8);
                voo.PartidaTempoReal = (DateTime)dr.GetValue(9);
                voo.ChegadaTempoReal = (DateTime)dr.GetValue(10);

                voo.Terminal = (Decimal)dr.GetValue(11);
                voo.CheckinInicio = (DateTime)dr.GetValue(12);
                voo.CheckinFim = (DateTime)dr.GetValue(13);
                voo.PortaEmbarque = (Decimal)dr.GetValue(14);
                voo.Embarque = (DateTime)dr.GetValue(15);
                voo.TapeteBagagem = (Decimal)dr.GetValue(16);
                voo.Bagagem = (DateTime)dr.GetValue(17);
                voo.PortaDesembarque = (Decimal)dr.GetValue(18);
                voo.Desembarque = (DateTime)dr.GetValue(19);

                listVoos.Add(voo);
            }
            conn.Dispose();

            return listVoos;
        }


        //Chegadas
        public static List<Model.Voo> ListChegadas(string id)
        {
            Voo voo = null;
            List<Model.Voo> listVoos = new List<Model.Voo>();

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();
            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_voo where chegadaaeroportoid = " + id;
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                voo = new Voo();
                voo.Id = (Decimal)dr.GetValue(0);
                voo.CodigoVoo = (Decimal)dr.GetValue(1);
                voo.CodigoCompanhia = (string)dr.GetValue(2);
                voo.PartidaAeroportoId = (Decimal)dr.GetValue(3);
                voo.ChegadaAeroportoId = (Decimal)dr.GetValue(4);
                voo.PartidaCidade = (String)dr.GetValue(5);
                voo.ChegadaCidade = (String)dr.GetValue(6);
                voo.PartidaTempoEstimado = (DateTime)dr.GetValue(7);
                voo.ChegadaTempoEstimado = (DateTime)dr.GetValue(8);
                voo.PartidaTempoReal = (DateTime)dr.GetValue(9);
                voo.ChegadaTempoReal = (DateTime)dr.GetValue(10);

                voo.Terminal = (Decimal)dr.GetValue(11);
                voo.CheckinInicio = (DateTime)dr.GetValue(12);
                voo.CheckinFim = (DateTime)dr.GetValue(13);
                voo.PortaEmbarque = (Decimal)dr.GetValue(14);
                voo.Embarque = (DateTime)dr.GetValue(15);
                voo.TapeteBagagem = (Decimal)dr.GetValue(16);
                voo.Bagagem = (DateTime)dr.GetValue(17);
                voo.PortaDesembarque = (Decimal)dr.GetValue(18);
                voo.Desembarque = (DateTime)dr.GetValue(19);

                listVoos.Add(voo);
            }
            conn.Dispose();

            return listVoos;
        }
    }
}