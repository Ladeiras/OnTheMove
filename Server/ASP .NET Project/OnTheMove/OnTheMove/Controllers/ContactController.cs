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
    public class ContactController : ApiController
    {
        //
        // GET: /Contact/

        public IEnumerable<Contact> Get(string id, string type)
        {
            return Lib_Contact.GetContacts(id,type);
        }

    }
}
