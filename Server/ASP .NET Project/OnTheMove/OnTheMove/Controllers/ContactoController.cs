using OnTheMove.Lib;
using OnTheMove.Lib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace OnTheMove.Controllers
{
    public class ContactoController : ApiController
    {
        //
        // GET: /Taxi/

        public Contacto Get(string id, string contact)
        {
            return Lib_Contacto.GetContacto(id,contact);
        }

    }
}
