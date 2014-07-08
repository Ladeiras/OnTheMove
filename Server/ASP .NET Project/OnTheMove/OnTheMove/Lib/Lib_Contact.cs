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
    public class Lib_Contact
    {
        public static List<Model.Contact> GetContacts(string id, string type)
        {
            Contact contact = null;
            List<Model.Contact> listContacts = new List<Model.Contact>();

            string onthemove = "DATA SOURCE=" + OnTheMove.WebApiConfig.DBSERVER + ";PERSIST SECURITY INFO=True;USER ID=SYSTEM;PASSWORD=123456;";
            OracleConnection conn = new OracleConnection(onthemove);
            conn.Open();

            OracleCommand cmd = new OracleCommand();
            cmd.Connection = conn;
            cmd.CommandText = "select * from otm_contact where type = '" + type + "' and airportcode = '" + id + "'";
            cmd.CommandType = CommandType.Text;
            OracleDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                contact = new Contact();

                if (dr.GetValue(3) == DBNull.Value)
                    contact.Code = "null";
                else
                    contact.Code = (string)dr.GetValue(3);

                if (dr.GetValue(4) == DBNull.Value)
                    contact.Email = "null";
                else
                    contact.Email = (string)dr.GetValue(4);

                if (dr.GetValue(5) == DBNull.Value)
                    contact.Facebook = "null";
                else
                    contact.Facebook = (string)dr.GetValue(5);

                if (dr.GetValue(6) == DBNull.Value)
                    contact.Telef = "null";
                else
                    contact.Telef = (string)dr.GetValue(6);

                if (dr.GetValue(7) == DBNull.Value)
                    contact.Twitter = "null";
                else
                    contact.Twitter = (string)dr.GetValue(7);

                if (dr.GetValue(8) == DBNull.Value)
                    contact.Website = "null";
                else
                    contact.Website = (string)dr.GetValue(8);

                if (dr.GetValue(9) == DBNull.Value)
                    contact.LogoUrl = "null";
                else
                    contact.LogoUrl = (string)dr.GetValue(9);

                listContacts.Add(contact);
            }
            conn.Dispose();

            return listContacts;
        }
    }
}